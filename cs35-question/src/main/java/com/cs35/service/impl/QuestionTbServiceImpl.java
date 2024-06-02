package com.cs35.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cs35.common.constant.HttpStatus;
import com.cs35.common.core.page.TableDataInfo;
import com.cs35.common.utils.SecurityUtils;
import com.cs35.common.utils.StringUtils;
import com.cs35.domain.*;
import com.cs35.enums.QuestionTypeEnum;
import com.cs35.mapper.*;
import com.cs35.service.IQuestionItemTbService;
import com.cs35.service.IQuestionTbService;
import com.cs35.service.IQuestionTypeTbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class QuestionTbServiceImpl extends ServiceImpl<QuestionTbMapper, QuestionTb> implements IQuestionTbService {


    @Autowired
    private IQuestionTypeTbService questionTypeTbService;

    @Autowired
    private IQuestionItemTbService questionItemTbService;

    @Autowired
    private GameTbMapper gameTbMapper;

    @Autowired
    private OptionAssociationTbMapper optionAssociationTbMapper;

    @Autowired
    private AnswerTbMapper answerTbMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private HistoryTbMapper historyTbMapper;
    @Autowired
    private GameCommentTbMapper gameCommentTbMapper;

    @Override
    public List<QuestionTb> selectQuestionTbList(QuestionTb questionTb) {
        List<QuestionTb> list = this.list();
        if (list != null && list.size() > 0) {
            list.forEach(item -> {
                QuestionTypeTb byId = questionTypeTbService.getById(item.getId());
                if (byId != null) {
                    item.setQuestionTypeNm(byId.getQuestionTypeNm());
                }
            });
        }
        return list;
    }

    @Override
    public QuestionTb selectQuestionTbById(Long id) {
        QuestionTb byId = this.getById(id);
        if (byId != null) {
            byId.setTypeValue(QuestionTypeEnum.getValue(byId.getType()));

            List<QuestionItemTb> list = questionItemTbService.list(Wrappers.lambdaQuery(QuestionItemTb.class)
                    .eq(QuestionItemTb::getQuestionId, byId.getId())
            );
            if (list != null && list.size() > 0) {
                list.forEach(item -> {
                    item.setCorrect(item.getResult() == 1);
                });
            }
            byId.setItemArr(list);

            QuestionTypeTb questionTypeTb = questionTypeTbService.getById(byId.getId());
            if (byId != null) {
                byId.setQuestionTypeNm(questionTypeTb.getQuestionTypeNm());
            }

        }
        return byId;
    }

    @Override
    public int insertQuestionTb(QuestionTb questionTb) {
        return this.save(questionTb) ? 1 : 0;
    }

    @Override
    public int updateQuestionTb(QuestionTb questionTb) {
        return this.updateById(questionTb) ? 1 : 0;
    }

    @Override
    public int deleteQuestionTbByIds(Long[] ids) {
        return 0;
    }


    @Override
    public int insertQuestionOption(QuestionTb questionTb) {
        if (questionTb != null) {
            if (questionTb.getId() != null) {
                questionItemTbService.remove(Wrappers.lambdaQuery(QuestionItemTb.class)
                        .eq(QuestionItemTb::getQuestionId, questionTb.getId())
                );
            }
            List<QuestionItemTb> itemArr = questionTb.getItemArr();
            if (itemArr != null && itemArr.size() > 0) {
                for (QuestionItemTb questionItemTb : itemArr) {
                    questionItemTb.setQuestionId(questionTb.getId());
                    if (questionItemTb.getCorrect()) {
                        questionItemTb.setResult(1);
                    } else {
                        questionItemTb.setResult(2);
                    }
                }
            }
            questionItemTbService.saveBatch(itemArr);
        }
        return 1;
    }


    @Override
    @Transactional
    public int createGame(GameTb gameTb) {
        if (gameTb.getId() != null) {
            gameTbMapper.updateById(gameTb);
            if (StringUtils.isNotEmpty(gameTb.getFields())) {
                List<JSONObject> jsonObjects = JSONArray.parseArray(gameTb.getFields(), JSONObject.class);
                if (jsonObjects != null && jsonObjects.size() > 0) {
                    List<QuestionTb> list = list(Wrappers.lambdaQuery(QuestionTb.class).eq(QuestionTb::getGameId, gameTb.getId()));
                    List<Long> collect = list.stream().map(o -> o.getId()).collect(Collectors.toList());
                    for (JSONObject jsonObject : jsonObjects) {
                        QuestionTb questionTb = new QuestionTb();
                        Long id = jsonObject.getLong("id");
                        questionTb.setQuestionTitle(jsonObject.getString("title"));
                        questionTb.setGameId(gameTb.getId());
                        questionTb.setType(jsonObject.getString("type"));
                        questionTb.setHint(jsonObject.getString("hint"));
                        questionTb.setAnswer(jsonObject.getString("answer"));
                        if (collect.contains(id)) {
                            questionTb.setId(id);
                            updateById(questionTb);
                            collect.remove(id);
                        } else {
                            this.save(questionTb);
                        }
                        JSONArray questionItem = jsonObject.getJSONArray("questionItem");
                        if (questionItem != null && questionItem.size() > 0) {
                            List<QuestionItemTb> list1 = questionItemTbService.list(Wrappers.lambdaQuery(QuestionItemTb.class).eq(QuestionItemTb::getQuestionId, id));
                            List<Long> collect1 = list1.stream().map(i -> i.getId()).collect(Collectors.toList());
                            for (Object o : questionItem) {
                                JSONObject item = (JSONObject) o;
                                Long id1 = item.getLong("id");
                                QuestionItemTb questionItemTb = new QuestionItemTb();
                                questionItemTb.setQuestionId(questionTb.getId());
                                questionItemTb.setItem(item.getString("content"));
                                questionItemTb.setScore(item.getString("score"));
                                if (collect1.contains(id1)) {
                                    questionItemTb.setId(id1);
                                    questionItemTbService.updateById(questionItemTb);
                                    collect1.remove(id1);
                                } else {
                                    questionItemTbService.save(questionItemTb);
                                }
                            }
                            if (collect1 != null && collect1.size() > 0) {
                                questionItemTbService.removeByIds(collect1);
                            }
                        }
                        JSONArray relationItems = jsonObject.getJSONArray("relationItems");
                        if (relationItems != null && relationItems.size() > 0) {
                            optionAssociationTbMapper.delete(Wrappers.lambdaQuery(OptionAssociationTb.class)
                                    .eq(OptionAssociationTb::getGameId, gameTb.getId())
                                    .eq(OptionAssociationTb::getQuestionId, questionTb.getId())
                            );
                            for (Object relationItem : relationItems) {
                                JSONObject item = (JSONObject) relationItem;
                                if (item.size() > 0) {
                                    String option = item.getString("option");
                                    String subject = item.getString("subject");

                                    OptionAssociationTb optionAssociationTb = new OptionAssociationTb();
                                    optionAssociationTb.setOptions(option);
                                    optionAssociationTb.setSkipQuestion(subject);
                                    optionAssociationTb.setGameId(gameTb.getId());
                                    optionAssociationTb.setQuestionId(questionTb.getId());
                                    optionAssociationTbMapper.insert(optionAssociationTb);
                                }
                            }
                        }
                    }
                    if (collect != null && collect.size() > 0) {
                        removeByIds(collect);
                    }
                }
            }
        } else {
            gameTbMapper.insert(gameTb);
            if (StringUtils.isNotEmpty(gameTb.getFields())) {
                List<JSONObject> jsonObjects = JSONArray.parseArray(gameTb.getFields(), JSONObject.class);
                if (jsonObjects != null && jsonObjects.size() > 0) {
                    for (JSONObject jsonObject : jsonObjects) {
                        QuestionTb questionTb = new QuestionTb();
                        questionTb.setGameId(gameTb.getId());
                        questionTb.setQuestionTitle(jsonObject.getString("title"));
                        questionTb.setType(jsonObject.getString("type"));
                        questionTb.setHint(jsonObject.getString("hint"));
                        questionTb.setAnswer(jsonObject.getString("answer"));
                        this.save(questionTb);
                        JSONArray questionItem = jsonObject.getJSONArray("questionItem");
                        if (questionItem != null && questionItem.size() > 0) {
                            for (Object o : questionItem) {
                                JSONObject item = (JSONObject) o;
                                QuestionItemTb questionItemTb = new QuestionItemTb();
                                questionItemTb.setQuestionId(questionTb.getId());
                                questionItemTb.setItem(item.getString("content"));
                                questionItemTb.setScore(item.getString("score"));
                                questionItemTbService.save(questionItemTb);
                            }
                        }
                        JSONArray relationItems = jsonObject.getJSONArray("relationItems");
                        if (relationItems != null && relationItems.size() > 0) {
                            for (Object relationItem : relationItems) {
                                JSONObject item = (JSONObject) relationItem;
                                if (item.size() > 0) {
                                    String option = item.getString("option");
                                    String subject = item.getString("subject");
                                    OptionAssociationTb optionAssociationTb = new OptionAssociationTb();
                                    optionAssociationTb.setOptions(option);
                                    optionAssociationTb.setSkipQuestion(subject);
                                    optionAssociationTb.setGameId(gameTb.getId());
                                    optionAssociationTb.setQuestionId(questionTb.getId());
                                    optionAssociationTbMapper.insert(optionAssociationTb);

                                }
                            }
                        }
                    }
                }
            }
        }
        return 1;
    }


    @Override
    public GameTb gameInfo(Long gameId) {
        GameTb gameTb = gameTbMapper.selectById(gameId);
        if (gameTb != null) {
            List<QuestionTb> list = this.list(Wrappers.lambdaQuery(QuestionTb.class).eq(QuestionTb::getGameId, gameId));
            if (list != null && list.size() > 0) {
                for (QuestionTb questionTb : list) {
                    questionTb.setTitle(questionTb.getQuestionTitle());
                    questionTb.setRequire(false);
                    List<QuestionItemTb> list1 = questionItemTbService.list(Wrappers.lambdaQuery(QuestionItemTb.class).eq(QuestionItemTb::getQuestionId, questionTb.getId()));
                    if (list1 != null && list1.size() > 0) {
                        for (QuestionItemTb questionItemTb : list1) {
                            questionItemTb.setContent(questionItemTb.getItem());
                            questionItemTb.setIsShow(false);
                            questionItemTb.setIsRepulsion(false);
                        }
                    }
                    questionTb.setQuestionItem(list1);
                    List<Map<String, Object>> relationItems = new ArrayList<>();

                    List<OptionAssociationTb> optionAssociationTbs = optionAssociationTbMapper.selectList(Wrappers.lambdaQuery(OptionAssociationTb.class)
                            .eq(OptionAssociationTb::getGameId, gameId)
                            .eq(OptionAssociationTb::getQuestionId, questionTb.getId()));
                    for (OptionAssociationTb optionAssociationTb : optionAssociationTbs) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("option", optionAssociationTb.getOptions());
                        map.put("subject", optionAssociationTb.getSkipQuestion());
                        relationItems.add(map);
                    }
                    questionTb.setRelationItems(relationItems);
                }
            }
            gameTb.setFields(JSONArray.toJSONString(list));
        }
        return gameTb;
    }


    @Override
    public List<GameTb> gameList(GameTb gameTb) {
        List<GameTb> gameTbs = gameTbMapper.selectList(Wrappers.lambdaQuery(GameTb.class)
                .last("  ORDER BY   sort is null asc, sort asc"));
        if (gameTbs != null && gameTbs.size() > 0) {
            int i = 0;
            int a = 0;
            for (GameTb game : gameTbs) {
                i++;
                List<HistoryTb> historyTbs = historyTbMapper.selectList(Wrappers.lambdaQuery(HistoryTb.class)
                        .eq(HistoryTb::getGameId, game.getId())
                        .eq(HistoryTb::getUserId, SecurityUtils.getUserId())
                        .eq(HistoryTb::getGameType, game.getQuestionTypeId())
                );

                if (historyTbs != null && historyTbs.size() > 0) {
                    List<Long> collect = historyTbs.stream().map(o -> o.getGameId()).distinct().collect(Collectors.toList());
                    if (collect.contains(game.getId())) {
                        a = i;
                    }
                }
                QuestionTypeTb questionTypeTb = questionTypeTbService.getById(game.getQuestionTypeId());
                if (questionTypeTb != null) {
                    game.setQuestionTypeNm(questionTypeTb.getQuestionTypeNm());
                }
            }
            if (a < gameTbs.size()) {
                gameTbs.get(a).setHistoryId(0L);
            }
        }
        return gameTbs;
    }


    @Override
    public QuestionTb startAnswer(QuestionTb questionTb1) {
        QuestionTb questionTb = null;
        if (questionTb1.getId() == null) {
            List<QuestionTb> list = this.list(Wrappers.lambdaQuery(QuestionTb.class).eq(QuestionTb::getGameId, questionTb1.getGameId())
                    .orderByAsc(QuestionTb::getId));
            if (list != null && list.size() > 0) {
                questionTb = list.get(0);
                questionTb.setTitle(questionTb.getQuestionTitle());
                questionTb.setRequire(false);
                List<QuestionItemTb> list1 = questionItemTbService.list(Wrappers.lambdaQuery(QuestionItemTb.class).eq(QuestionItemTb::getQuestionId, questionTb.getId()));
                if (list1 != null && list1.size() > 0) {
                    for (QuestionItemTb questionItemTb : list1) {
                        questionItemTb.setContent(questionItemTb.getItem());
                        questionItemTb.setIsShow(false);
                        questionItemTb.setIsRepulsion(false);
                    }
                }
                questionTb.setQuestionItem(list1);
                questionTb.setSort(1);
                //

                List<Map<String, Object>> maps = jdbcTemplate.queryForList("select max(id) id from history_tb ");
                if (maps != null && maps.size() > 0) {
                    questionTb.setHistoryId(maps.get(0).get("id") == null ? 1L : (Long.parseLong(maps.get(0).get("id").toString()) + 1L));
                } else {
                    questionTb.setHistoryId(1L);
                }
                return questionTb;
            }
        } else {

            HistoryTb historyTb1 = historyTbMapper.selectById(questionTb1.getHistoryId());
            if (historyTb1 == null) {
                GameTb gameTb = gameTbMapper.selectById(questionTb1.getGameId());
                HistoryTb historyTb = new HistoryTb();
                historyTb.setId(questionTb1.getHistoryId());
                historyTb.setGameId(questionTb1.getGameId());
                historyTb.setUserId(SecurityUtils.getUserId());
                historyTb.setGameType(gameTb.getQuestionTypeId());
                historyTb.setCreateTime(new Date());
                historyTbMapper.insert(historyTb);
            }

            AnswerTb answerTb = new AnswerTb();
            answerTb.setGameId(questionTb1.getGameId());
            answerTb.setQuestionId(questionTb1.getId());
            answerTb.setOptions(questionTb1.getOptions());
            answerTb.setSort(questionTb1.getSort());
            answerTb.setUserId(SecurityUtils.getUserId());
            answerTb.setAnswerTime(questionTb1.getAnswerTime());
            int score = 0;
            List<String> selectList = new ArrayList<>();
            if (questionTb1.getOptions().contains("[")) {
                String item = questionTb1.getOptions().substring(0);
                item = item.substring(0, item.length() - 1);
                selectList = Arrays.asList(item.split(","));
            } else {
                selectList.add(questionTb1.getOptions());
            }
            List<QuestionItemTb> questionItem = questionTb1.getQuestionItem();
            for (QuestionItemTb questionItemTb : questionItem) {
                if (selectList.contains(questionItemTb.getId().toString())) {
                    score += (questionItemTb.getScore() == null ? 0 : Integer.parseInt(questionItemTb.getScore()));
                }
            }
            answerTb.setScore(score);
            answerTb.setHistoryId(questionTb1.getHistoryId());
            answerTbMapper.insert(answerTb);
            List<OptionAssociationTb> optionAssociationTbs = optionAssociationTbMapper.selectList(Wrappers.lambdaQuery(OptionAssociationTb.class)
                    .eq(OptionAssociationTb::getGameId, answerTb.getGameId())
                    .eq(OptionAssociationTb::getQuestionId, answerTb.getQuestionId())
                    .eq(OptionAssociationTb::getOptions, answerTb.getOptions()));

            if (optionAssociationTbs != null && optionAssociationTbs.size() > 0) {
                String skipQuestion = optionAssociationTbs.get(0).getSkipQuestion();
                if (StringUtils.isNotEmpty(skipQuestion)) {
                    questionTb = this.getById(skipQuestion);
                }
            }
            if (questionTb == null) {
                List<QuestionTb> list = this.list(Wrappers.lambdaQuery(QuestionTb.class).eq(QuestionTb::getGameId, answerTb.getGameId())
                        .orderByAsc(QuestionTb::getId));
                if (list != null && list.size() > 0) {
                    int i = 0;
                    for (QuestionTb tb : list) {
                        i++;
                        if (Objects.equals(answerTb.getQuestionId(), tb.getId())) {
                            if (list.size() > i) {
                                questionTb = list.get(i);
                                break;
                            }
                        }
                    }
                }
            }
            if (questionTb != null) {
                questionTb.setTitle(questionTb.getQuestionTitle());
                questionTb.setRequire(false);
                questionTb.setHistoryId(answerTb.getHistoryId());
                List<QuestionItemTb> list1 = questionItemTbService.list(Wrappers.lambdaQuery(QuestionItemTb.class).eq(QuestionItemTb::getQuestionId, questionTb.getId()));
                if (list1 != null && list1.size() > 0) {
                    for (QuestionItemTb questionItemTb : list1) {
                        questionItemTb.setContent(questionItemTb.getItem());
                        questionItemTb.setIsShow(false);
                        questionItemTb.setIsRepulsion(false);
                    }
                }
                questionTb.setQuestionItem(list1);
                questionTb.setSort(answerTb.getSort() + 1);
                return questionTb;
            }
        }
        questionTb = new QuestionTb();
        questionTb.setEnd(true);
        return questionTb;
    }


    @Override
    public TableDataInfo historyList(Integer pageNum, Integer pageSize) {
        String wheresql = " ";
        List<Map<String, Object>> maps1 = jdbcTemplate.queryForList("SELECT\n" +
                "\tsr.role_key roleCode \n" +
                "FROM\n" +
                "\tsys_user_role sur\n" +
                "\tLEFT JOIN sys_user su ON su.user_id = sur.user_id\n" +
                "\tLEFT JOIN sys_role sr ON sr.role_id = sur.role_id\t\n" +
                "\tWHERE sur.user_id = " + SecurityUtils.getUserId());
        if (maps1 != null && maps1.size() > 0) {
            List<String> roleCode = maps1.stream().map(o -> {
                return o.get("roleCode").toString();
            }).collect(Collectors.toList());
            if (roleCode.contains("Teacher") || roleCode.contains("admin")) {
                wheresql += "     ";
            } else {
                wheresql += "  and t1.user_id=" + SecurityUtils.getUserId();
            }
        }
        String sql = "SELECT t1.id historyId,t1.evaluate,t2.*," +
                "( SELECT nick_name FROM sys_user WHERE user_id = t1.user_id ) nickName," +
                "( SELECT MAX( answer_time ) FROM answer_tb WHERE history_id = t1.id AND game_id = t2.id ) answerTime ," +
                "t1.create_time createTime\n" +
                "FROM\n" +
                "\thistory_tb t1\n" +
                "\tLEFT JOIN game_tb t2 ON t1.game_id = t2.id \n" +
                "WHERE  1=1 " + wheresql;
        Long total = 0L;
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select count(1) total from (" + sql + ") t");
        if (maps != null && maps.size() > 0) {
            total = maps.get(0).get("total") == null ? 0L : Long.parseLong(maps.get(0).get("total").toString());
        }
        List<GameTb> query = jdbcTemplate.query("select t.* from (" + sql + ") t limit " + (pageNum - 1) * pageSize + "," + pageSize, new BeanPropertyRowMapper<>(GameTb.class));
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(HttpStatus.SUCCESS);
        rspData.setMsg("");
        rspData.setRows(query);
        rspData.setTotal(total);
        return rspData;
    }


    @Override
    public GameTb historyInfo(GameTb param) {
        GameTb gameTb = gameTbMapper.selectById(param.getId());
        if (gameTb != null) {
            List<QuestionTb> list = jdbcTemplate.query("SELECT\n" +
                    "\tt2.*,\n" +
                    "\tt1.`options` choice  " +
                    "FROM\n" +
                    "\tanswer_tb t1\n" +
                    "\tLEFT JOIN question_tb t2 ON t1.question_id = t2.id \n" +
                    "WHERE\n" +
                    "\tt1.game_id = " + param.getId() + " \n" +
                    "\tAND t1.history_id = " + param.getHistoryId() + " \n" +
                    "ORDER BY\n" +
                    "\tt1.sort", new BeanPropertyRowMapper<>(QuestionTb.class));
            if (list != null && list.size() > 0) {
                for (QuestionTb questionTb : list) {
                    questionTb.setTitle(questionTb.getQuestionTitle());
                    questionTb.setRequire(false);
                    List<QuestionItemTb> list1 = questionItemTbService.list(Wrappers.lambdaQuery(QuestionItemTb.class).eq(QuestionItemTb::getQuestionId, questionTb.getId()));
                    if (list1 != null && list1.size() > 0) {
                        for (QuestionItemTb questionItemTb : list1) {
                            if ("radio".equals(questionTb.getType())) {
                                if (questionTb.getChoice().equals(questionItemTb.getId().toString())) {
                                    questionItemTb.setScore("1");
                                } else {
                                    questionItemTb.setScore("0");
                                }
                            }
                            if ("checkbox".equals(questionTb.getType()) && StringUtils.isNotEmpty(questionTb.getChoice())) {
                                String substring = questionTb.getChoice().substring(1);
                                substring = substring.substring(0, substring.length() - 1);
                                List<String> list2 = Arrays.asList(substring.split(","));
                                if (list2.contains(questionItemTb.getId().toString())) {
                                    questionItemTb.setScore("1");
                                } else {
                                    questionItemTb.setScore("0");
                                }
                            }
                            questionItemTb.setContent(questionItemTb.getItem());
                            questionItemTb.setIsShow(false);
                            questionItemTb.setIsRepulsion(false);
                        }
                    }
                    questionTb.setQuestionItem(list1);
                    List<Map<String, Object>> relationItems = new ArrayList<>();
                    List<OptionAssociationTb> optionAssociationTbs = optionAssociationTbMapper.selectList(Wrappers.lambdaQuery(OptionAssociationTb.class)
                            .eq(OptionAssociationTb::getGameId, param.getId())
                            .eq(OptionAssociationTb::getQuestionId, questionTb.getId()));
                    for (OptionAssociationTb optionAssociationTb : optionAssociationTbs) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("option", optionAssociationTb.getOptions());
                        map.put("subject", optionAssociationTb.getSkipQuestion());
                        relationItems.add(map);
                    }
                    questionTb.setRelationItems(relationItems);
                }
            }
            gameTb.setFields(JSONArray.toJSONString(list));
        }
        return gameTb;
    }


    @Override
    public int deleteGame(Long gameId) {
        return gameTbMapper.deleteById(gameId);
    }


    @Override
    public int historyEvaluate(HistoryTb historyTb) {
        return historyTbMapper.updateById(historyTb);
    }

    @Override
    public HistoryTb getEvaluate(Long id) {
        return historyTbMapper.selectById(id);
    }


    @Override
    public List<Map<String, Object>> integralList() {
        return jdbcTemplate.queryForList("SELECT\n" +
                "\tSUM( score ) total,\n" +
                "\tuser_id userId,\n" +
                "\t( SELECT nick_name FROM sys_user WHERE user_id = t1.user_id ) nickName \n" +
                "FROM\n" +
                "\t(\n" +
                "\tSELECT\n" +
                "\t\thistory_id,\n" +
                "\t\tgame_id,\n" +
                "\t\tSUM( score )+ CEIL( 100- FLOOR(( TIME_TO_SEC( max( answer_time )))/ 300 )* 10 ) score,\n" +
                "\t\tuser_id \n" +
                "\tFROM\n" +
                "\t\tanswer_tb \n" +
                "\tGROUP BY\n" +
                "\t\tgame_id,\n" +
                "\t\thistory_id \n" +
                "\t) t1 \n" +
                "WHERE\n" +
                "\tEXISTS ( SELECT * FROM game_tb t2 WHERE t1.game_id = t2.id ) \n" +
                "\tAND EXISTS ( SELECT * FROM history_tb t3 WHERE t1.history_id = t3.id ) \n" +
                "GROUP BY\n" +
                "\tuser_id \n" +
                "ORDER BY\n" +
                "\tSUM( score ) DESC");
    }


    @Override
    public Integer gameComment(GameCommentTb gameCommentTb) {
        gameCommentTb.setUserId(SecurityUtils.getUserId());
        gameCommentTb.setUserName(SecurityUtils.getUsername());
        gameCommentTb.setCreateTime(new Date());
        return gameCommentTbMapper.insert(gameCommentTb);
    }


    @Override
    public List<GameCommentTb> getGameComment(GameCommentTb gameCommentTb) {
        return gameCommentTbMapper.selectList(Wrappers.lambdaQuery(GameCommentTb.class)
                .eq(GameCommentTb::getGameId, gameCommentTb.getGameId())
                .orderByDesc(GameCommentTb::getCreateTime)
        );
    }


    @Override
    public List<Map<String, Object>> answerCancel(AnswerTb answerTb) {
        return jdbcTemplate.queryForList("SELECT\n" +
                "\tSUM( score ) total,answer_time answerTime,\n" +
                "\tuser_id userId,\n" +
                "\t( SELECT nick_name FROM sys_user WHERE user_id = t1.user_id ) nickName \n" +
                "FROM\n" +
                "\t(\n" +
                "\tSELECT\n" +
                "\t\thistory_id,\n" +
                "\t\tgame_id,max( answer_time ) answer_time,\n" +
                "\t\tSUM( score )+ CEIL( 100- FLOOR(( TIME_TO_SEC( max( answer_time )))/ 300 )* 10 ) score,\n" +
                "\t\tuser_id \n" +
                "\tFROM\n" +
                "\t\tanswer_tb  where history_id = " + answerTb.getHistoryId() + "  and game_id=" + answerTb.getGameId() + "  and user_id=" + SecurityUtils.getUserId() + " " +
                "\tGROUP BY\n" +
                "\t\tgame_id,\n" +
                "\t\thistory_id \n" +
                "\t) t1 \n" +
                "WHERE\n" +
                "\tEXISTS ( SELECT * FROM game_tb t2 WHERE t1.game_id = t2.id ) \n" +
                "\tAND EXISTS ( SELECT * FROM history_tb t3 WHERE t1.history_id = t3.id ) \n" +
                "GROUP BY\n" +
                "\tuser_id \n" +
                "ORDER BY\n" +
                "\tSUM( score ) DESC");
    }
}
