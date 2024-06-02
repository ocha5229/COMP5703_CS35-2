"use strict";
const path = require("path");

function resolve(dir) {
  return path.join(__dirname, dir);
}

const CompressionPlugin = require("compression-webpack-plugin");

const name = process.env.VUE_APP_TITLE || "PM playground"; //

const port = process.env.port || process.env.npm_config_port || 8080; //

// vue.config.js
//vue.config.js  https://cli.vuejs.org/zh/config/#css-loaderoptions
// ，
module.exports = {
  // URL。
  // ，Vue CLI
  publicPath: process.env.NODE_ENV === "production" ? "/" : "/",
  // npm run build  yarn build  ，（baseUrl）（dist）
  outputDir: "dist",
  //  (js、css、img、fonts) ；（，）
  assetsDir: "static",
  // eslint，：ture | false | 'error'
  lintOnSave: process.env.NODE_ENV === "development",
  //  source map， false 。
  productionSourceMap: false,
  // webpack-dev-server
  devServer: {
    host: "0.0.0.0",
    port: port,
    open: true,
    proxy: {
      // detail: https://cli.vuejs.org/config/#devserver-proxy
      [process.env.VUE_APP_BASE_API]: {
        // target: `http://localhost:8081`,

        // target: "https://2016kt6872.oicp.vip",
        target: `http://52.64.218.116`,
        //`target: "http://182.92.192.47:8080",
        changeOrigin: true,
        pathRewrite: {
          ["^" + process.env.VUE_APP_BASE_API]: "",
        },
      },
    },
    disableHostCheck: false, // ip
  },
  css: {
    loaderOptions: {
      sass: {
        sassOptions: { outputStyle: "expanded" },
      },
    },
  },
  configureWebpack: {
    name: name,
    resolve: {
      alias: {
        "@": resolve("src"),
      },
    },
    devtool: "source-map",
    plugins: [
      new CompressionPlugin({
        cache: false, //
        test: /\.(js|css|html|jpe?g|png|gif|svg)?$/i, //
        filename: "[path][base].gz[query]", //
        algorithm: "gzip", // gzip
        minRatio: 0.8, // ， 80%
        deleteOriginalAssets: false, //
      }),
    ],
  },
  chainWebpack(config) {
    config.plugins.delete("preload"); // TODO: need test
    config.plugins.delete("prefetch"); // TODO: need test

    // set svg-sprite-loader
    config.module.rule("svg").exclude.add(resolve("src/assets/icons")).end();
    config.module
      .rule("icons")
      .test(/\.svg$/)
      .include.add(resolve("src/assets/icons"))
      .end()
      .use("svg-sprite-loader")
      .loader("svg-sprite-loader")
      .options({
        symbolId: "icon-[name]",
      })
      .end();

    config.when(process.env.NODE_ENV !== "development", (config) => {
      config
        .plugin("ScriptExtHtmlWebpackPlugin")
        .after("html")
        .use("script-ext-html-webpack-plugin", [
          {
            // `runtime` must same as runtimeChunk name. default is `runtime`
            inline: /runtime\..*\.js$/,
          },
        ])
        .end();

      config.optimization.splitChunks({
        chunks: "all",
        cacheGroups: {
          libs: {
            name: "chunk-libs",
            test: /[\\/]node_modules[\\/]/,
            priority: 10,
            chunks: "initial", // only package third parties that are initially dependent
          },
          elementUI: {
            name: "chunk-elementUI", // split elementUI into a single package
            test: /[\\/]node_modules[\\/]_?element-ui(.*)/, // in order to adapt to cnpm
            priority: 20, // the weight needs to be larger than libs and app or it will be packaged into libs or app
          },
          commons: {
            name: "chunk-commons",
            test: resolve("src/components"), // can customize your rules
            minChunks: 3, //  minimum common number
            priority: 5,
            reuseExistingChunk: true,
          },
        },
      });
      config.optimization.runtimeChunk("single");
    });
  },
};
