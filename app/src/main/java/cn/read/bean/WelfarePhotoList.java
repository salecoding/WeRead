package cn.read.bean;

import java.util.List;

/**
 * Created by lw on 2017/3/10.
 */
public class WelfarePhotoList {

    /**
     * error : false
     * results : [{"_id":"58c1f808421aa95810795c34","createdAt":"2017-03-10T08:49:12.756Z","desc":"3-10","publishedAt":"2017-03-10T11:43:50.30Z","source":"chrome","type":"福利","url":"http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-10-17127037_231706780569079_1119464847537340416_n.jpg","used":true,"who":"代码家"},{"_id":"58c0ac1f421aa90f03345143","createdAt":"2017-03-09T09:13:03.655Z","desc":"3-9","publishedAt":"2017-03-09T11:42:30.849Z","source":"chrome","type":"福利","url":"http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-09-17127109_1652837611687612_1425055271046086656_n.jpg","used":true,"who":"daimajia"},{"_id":"58bf522a421aa90efc4fb689","createdAt":"2017-03-08T08:36:58.695Z","desc":"3-8","publishedAt":"2017-03-08T11:27:16.65Z","source":"chrome","type":"福利","url":"http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-08-17126216_1253875034703554_7520300169779216384_n.jpg","used":true,"who":"daimajia"},{"_id":"58be00ad421aa95810795c13","createdAt":"2017-03-07T08:37:01.730Z","desc":"3-7","publishedAt":"2017-03-07T11:52:11.670Z","source":"chrome","type":"福利","url":"http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-07-003645.jpg","used":true,"who":"daimajia"},{"_id":"58bcae33421aa90efc4fb666","createdAt":"2017-03-06T08:32:51.490Z","desc":"3-6","publishedAt":"2017-03-06T11:17:33.140Z","source":"chrome","type":"福利","url":"http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-06-tumblr_ombicf0KXi1vpybydo6_540.jpg","used":true,"who":"daimajia"},{"_id":"58b88a2f421aa90efc4fb649","createdAt":"2017-03-03T05:10:07.940Z","desc":"3-3","publishedAt":"2017-03-03T12:13:36.333Z","source":"chrome","type":"福利","url":"http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-02-16906481_1495916493759925_5770648570629718016_n.jpg","used":true,"who":"daimajia"},{"_id":"58b76cb9421aa90f131785e4","createdAt":"2017-03-02T08:52:09.811Z","desc":"3-02","publishedAt":"2017-03-02T11:51:29.672Z","source":"chrome","type":"福利","url":"http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-02-17077393_314135475655975_7855162741030387712_n.jpg","used":true,"who":"代码家"},{"_id":"58b60881421aa90efc4fb627","createdAt":"2017-03-01T07:32:17.106Z","desc":"3-1","publishedAt":"2017-03-01T12:03:57.461Z","source":"chrome","type":"福利","url":"http://7xi8d6.com1.z0.glb.clouddn.com/2017-02-28-15057157_446684252387131_4267811446148038656_n.jpg","used":true,"who":"daimajia"},{"_id":"58b4b764421aa90efc4fb61b","createdAt":"2017-02-28T07:33:56.976Z","desc":"2-28","publishedAt":"2017-02-28T11:45:44.815Z","source":"chrome","type":"福利","url":"http://7xi8d6.com1.z0.glb.clouddn.com/2017-02-27-tumblr_om1aowIoKa1qbw5qso1_540.jpg","used":true,"who":"daimajia"},{"_id":"58b372a7421aa90f033450e0","createdAt":"2017-02-27T08:28:23.976Z","desc":"2-27","publishedAt":"2017-02-27T11:31:40.141Z","source":"chrome","type":"福利","url":"http://7xi8d6.com1.z0.glb.clouddn.com/2017-02-27-002809.jpg","used":true,"who":"daimajia"}]
     */

    private boolean error;
    private List<ResultsBean> results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * _id : 58c1f808421aa95810795c34
         * createdAt : 2017-03-10T08:49:12.756Z
         * desc : 3-10
         * publishedAt : 2017-03-10T11:43:50.30Z
         * source : chrome
         * type : 福利
         * url : http://7xi8d6.com1.z0.glb.clouddn.com/2017-03-10-17127037_231706780569079_1119464847537340416_n.jpg
         * used : true
         * who : 代码家
         */

        private String _id;
        private String createdAt;
        private String desc;
        private String publishedAt;
        private String source;
        private String type;
        private String url;
        private boolean used;
        private String who;

        public String get_id() {
            return _id;
        }

        public void set_id(String _id) {
            this._id = _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getPublishedAt() {
            return publishedAt;
        }

        public void setPublishedAt(String publishedAt) {
            this.publishedAt = publishedAt;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public boolean isUsed() {
            return used;
        }

        public void setUsed(boolean used) {
            this.used = used;
        }

        public String getWho() {
            return who;
        }

        public void setWho(String who) {
            this.who = who;
        }
    }
}
