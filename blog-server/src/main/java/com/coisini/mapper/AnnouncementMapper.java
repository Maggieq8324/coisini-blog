package com.coisini.mapper;

import com.coisini.entity.Announcement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * @Description 公告Mapper
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
@Repository
@Mapper
public interface AnnouncementMapper {

    /**
     * 保存公告
     * @param announcement
     */
    void saveAnnouncement(Announcement announcement);

    /**
     * 更新公告删除状态
     * @param announcementId
     */
    void deleteAnnouncementById(Integer announcementId);

    /**
     * 更新公告置顶状态
     * @param announcement
     */
    void updateAnnouncementTop(Announcement announcement);

    /**
     * 获取公告数量
     * @return
     */
    Long getAnnouncementCount();

    /**
     * 分页查询公告
     * @param start
     * @param showCount
     * @return
     */
    List<Announcement> findAnnouncement(@Param("start") Integer start, @Param("showCount") Integer showCount);
}
