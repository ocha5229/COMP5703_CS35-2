import Vue from 'vue'
import { mergeRecursive } from "@/utils/cs35";
import DictMeta from './DictMeta'
import DictData from './DictData'

const DEFAULT_DICT_OPTIONS = {
  types: [],
}

/**
 * @classdesc
 * @property {Object} label ，
 * @property {Object} dict ，
 * @property {Array.<DictMeta>} _dictMetas
 */
export default class Dict {
  constructor() {
    this.owner = null
    this.label = {}
    this.type = {}
  }

  init(options) {
    if (options instanceof Array) {
      options = { types: options }
    }
    const opts = mergeRecursive(DEFAULT_DICT_OPTIONS, options)
    if (opts.types === undefined) {
      throw new Error('need dict types')
    }
    const ps = []
    this._dictMetas = opts.types.map(t => DictMeta.parse(t))
    this._dictMetas.forEach(dictMeta => {
      const type = dictMeta.type
      Vue.set(this.label, type, {})
      Vue.set(this.type, type, [])
      if (dictMeta.lazy) {
        return
      }
      ps.push(loadDict(this, dictMeta))
    })
    return Promise.all(ps)
  }

  /**
   *
   * @param {String} type
   */
  reloadDict(type) {
    const dictMeta = this._dictMetas.find(e => e.type === type)
    if (dictMeta === undefined) {
      return Promise.reject(`the dict meta of ${type} was not found`)
    }
    return loadDict(this, dictMeta)
  }
}

/**
 *
 * @param {Dict} dict
 * @param {DictMeta} dictMeta
 * @returns {Promise}
 */
function loadDict(dict, dictMeta) {
  return dictMeta.request(dictMeta)
    .then(response => {
      const type = dictMeta.type
      let dicts = dictMeta.responseConverter(response, dictMeta)
      if (!(dicts instanceof Array)) {
        console.error('the return of responseConverter must be Array.<DictData>')
        dicts = []
      } else if (dicts.filter(d => d instanceof DictData).length !== dicts.length) {
        console.error('the type of elements in dicts must be DictData')
        dicts = []
      }
      dict.type[type].splice(0, Number.MAX_SAFE_INTEGER, ...dicts)
      dicts.forEach(d => {
        Vue.set(dict.label[type], d.value, d.label)
      })
      return dicts
    })
}
