package ajou.com.skechip.Retrofit.models;

import java.util.List;


public class GroupResponse {
    private boolean error;

    private List<Integer> idList;
    private List<Integer> managerList;
    private List<String> titleList;
    private List<String> tagList;
    private Integer totalCount;

    public GroupResponse(boolean error, List<Integer> idList, List<Integer> managerList, List<String> titleList, List<String> tagList, Integer totalCount) {
        this.error = error;
        this.idList = idList;
        this.managerList = managerList;
        this.titleList = titleList;
        this.tagList = tagList;
        this.totalCount = totalCount;
    }

    public boolean isError() {
        return error;
    }

    public List<Integer> getIdList() {
        return idList;
    }

    public List<Integer> getManagerList() {
        return managerList;
    }

    public List<String> getTitleList() {
        return titleList;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public Integer getTotalCount() {
        return totalCount;
    }
}