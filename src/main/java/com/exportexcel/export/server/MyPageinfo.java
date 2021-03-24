package com.exportexcel.export.server;

public class MyPageinfo {
        private Integer totalNum;
        private Integer totalPage;
        private Integer pageSize;
        private Integer pageIndex;
        private Integer queryIndex;

        public MyPageinfo subPaging(Integer totalNum, Integer pageSize, Integer pageIndex) {
            MyPageinfo page = new MyPageinfo();
            page.setTotalNum(totalNum);
            Integer totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;
            page.setTotalPage(totalPage);
            page.setPageIndex(pageIndex + 1);
            page.setPageSize(pageSize);
            page.setQueryIndex(pageSize * pageIndex);
            return page;

        }

        public Integer getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(Integer totalNum) {
            this.totalNum = totalNum;
        }

        public Integer getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(Integer totalPage) {
            this.totalPage = totalPage;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }

        public Integer getPageIndex() {
            return pageIndex;
        }

        public void setPageIndex(Integer pageIndex) {
            this.pageIndex = pageIndex;
        }

        public Integer getQueryIndex() {
            return queryIndex;
        }

        public void setQueryIndex(Integer queryIndex) {
            this.queryIndex = queryIndex;
        }

}
