package yc.com.homework.base.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import yc.com.answer.index.model.bean.BookAnswerInfo;
import yc.com.homework.index.domain.bean.CompositionInfo;
import yc.com.homework.index.domain.bean.SearchRecordInfo;
import yc.com.homework.read.domain.bean.BookInfo;
import yc.com.homework.word.model.domain.BookWordInfo;
import yc.com.homework.word.model.domain.GradeInfo;

import yc.com.homework.base.dao.BookAnswerInfoDao;
import yc.com.homework.base.dao.CompositionInfoDao;
import yc.com.homework.base.dao.SearchRecordInfoDao;
import yc.com.homework.base.dao.BookInfoDao;
import yc.com.homework.base.dao.BookWordInfoDao;
import yc.com.homework.base.dao.GradeInfoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig bookAnswerInfoDaoConfig;
    private final DaoConfig compositionInfoDaoConfig;
    private final DaoConfig searchRecordInfoDaoConfig;
    private final DaoConfig bookInfoDaoConfig;
    private final DaoConfig bookWordInfoDaoConfig;
    private final DaoConfig gradeInfoDaoConfig;

    private final BookAnswerInfoDao bookAnswerInfoDao;
    private final CompositionInfoDao compositionInfoDao;
    private final SearchRecordInfoDao searchRecordInfoDao;
    private final BookInfoDao bookInfoDao;
    private final BookWordInfoDao bookWordInfoDao;
    private final GradeInfoDao gradeInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        bookAnswerInfoDaoConfig = daoConfigMap.get(BookAnswerInfoDao.class).clone();
        bookAnswerInfoDaoConfig.initIdentityScope(type);

        compositionInfoDaoConfig = daoConfigMap.get(CompositionInfoDao.class).clone();
        compositionInfoDaoConfig.initIdentityScope(type);

        searchRecordInfoDaoConfig = daoConfigMap.get(SearchRecordInfoDao.class).clone();
        searchRecordInfoDaoConfig.initIdentityScope(type);

        bookInfoDaoConfig = daoConfigMap.get(BookInfoDao.class).clone();
        bookInfoDaoConfig.initIdentityScope(type);

        bookWordInfoDaoConfig = daoConfigMap.get(BookWordInfoDao.class).clone();
        bookWordInfoDaoConfig.initIdentityScope(type);

        gradeInfoDaoConfig = daoConfigMap.get(GradeInfoDao.class).clone();
        gradeInfoDaoConfig.initIdentityScope(type);

        bookAnswerInfoDao = new BookAnswerInfoDao(bookAnswerInfoDaoConfig, this);
        compositionInfoDao = new CompositionInfoDao(compositionInfoDaoConfig, this);
        searchRecordInfoDao = new SearchRecordInfoDao(searchRecordInfoDaoConfig, this);
        bookInfoDao = new BookInfoDao(bookInfoDaoConfig, this);
        bookWordInfoDao = new BookWordInfoDao(bookWordInfoDaoConfig, this);
        gradeInfoDao = new GradeInfoDao(gradeInfoDaoConfig, this);

        registerDao(BookAnswerInfo.class, bookAnswerInfoDao);
        registerDao(CompositionInfo.class, compositionInfoDao);
        registerDao(SearchRecordInfo.class, searchRecordInfoDao);
        registerDao(BookInfo.class, bookInfoDao);
        registerDao(BookWordInfo.class, bookWordInfoDao);
        registerDao(GradeInfo.class, gradeInfoDao);
    }
    
    public void clear() {
        bookAnswerInfoDaoConfig.clearIdentityScope();
        compositionInfoDaoConfig.clearIdentityScope();
        searchRecordInfoDaoConfig.clearIdentityScope();
        bookInfoDaoConfig.clearIdentityScope();
        bookWordInfoDaoConfig.clearIdentityScope();
        gradeInfoDaoConfig.clearIdentityScope();
    }

    public BookAnswerInfoDao getBookAnswerInfoDao() {
        return bookAnswerInfoDao;
    }

    public CompositionInfoDao getCompositionInfoDao() {
        return compositionInfoDao;
    }

    public SearchRecordInfoDao getSearchRecordInfoDao() {
        return searchRecordInfoDao;
    }

    public BookInfoDao getBookInfoDao() {
        return bookInfoDao;
    }

    public BookWordInfoDao getBookWordInfoDao() {
        return bookWordInfoDao;
    }

    public GradeInfoDao getGradeInfoDao() {
        return gradeInfoDao;
    }

}
