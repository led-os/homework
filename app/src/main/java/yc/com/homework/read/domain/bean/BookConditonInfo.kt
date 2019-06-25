package yc.com.homework.read.domain.bean

/**
 * Created by wanglin  on 2019/1/10 15:47.
 */
class BookConditonInfo {

    /**
     * versions : [{"version_id":0,"version_name":"人教版","sort":1},{"version_id":81,"version_name":"Join in","sort":50},{"version_id":53,"version_name":"人教新起点","sort":50},{"version_id":54,"version_name":"人教精通版","sort":50},{"version_id":55,"version_name":"冀教版三起","sort":50},{"version_id":56,"version_name":"EEC版","sort":50},{"version_id":58,"version_name":"北师大版三起","sort":50},{"version_id":62,"version_name":"湘少版","sort":50},{"version_id":69,"version_name":"语文S版","sort":50},{"version_id":70,"version_name":"闽教版","sort":50},{"version_id":72,"version_name":"清华版","sort":50},{"version_id":73,"version_name":"重大版","sort":50},{"version_id":74,"version_name":"辽师大版","sort":50},{"version_id":75,"version_name":"辽师大版三起","sort":50},{"version_id":79,"version_name":"新蕾版","sort":50},{"version_id":80,"version_name":"湘鲁教版","sort":50},{"version_id":52,"version_name":"人教灵通版","sort":50},{"version_id":34,"version_name":"粤人版","sort":50},{"version_id":32,"version_name":"陕旅版","sort":50},{"version_id":1,"version_name":"北师大版","sort":50},{"version_id":3,"version_name":"译林版","sort":50},{"version_id":4,"version_name":"北京课改版","sort":50},{"version_id":5,"version_name":"冀教版","sort":50},{"version_id":6,"version_name":"鲁教版","sort":50},{"version_id":7,"version_name":"教科版","sort":50},{"version_id":8,"version_name":"沪教版","sort":50},{"version_id":10,"version_name":"苏教版","sort":50},{"version_id":16,"version_name":"语文版","sort":50},{"version_id":22,"version_name":"新世纪版","sort":50},{"version_id":23,"version_name":"牛津全国版","sort":50},{"version_id":25,"version_name":"粤教版","sort":50},{"version_id":26,"version_name":"川教版","sort":50},{"version_id":27,"version_name":"仁爱版","sort":50},{"version_id":-1,"version_name":"其他","sort":90}]
     * grades : {"junior":[{"grade":1,"grade_name":"一年级"},{"grade":2,"grade_name":"二年级"},{"grade":3,"grade_name":"三年级"},{"grade":4,"grade_name":"四年级"},{"grade":5,"grade_name":"五年级"},{"grade":6,"grade_name":"六年级"}],"middle":[{"grade":7,"grade_name":"七年级"},{"grade":8,"grade_name":"八年级"},{"grade":9,"grade_name":"九年级"}]}
     */

    var grades: GradeInfoWrapper? = null
    var versions: ArrayList<GradeVersionInfo>? = null
    var subjects: ArrayList<GradeVersionInfo>? = null
}
