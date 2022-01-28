package com.coisini.service;

import com.coisini.entity.Announcement;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface AnnouncementService {

    /**
     * 保存公告
     * @param announcementTitle
     * @param announcementBody
     */
    void saveAnnouncement(String announcementTitle, String announcementBody);

    /**
     * 根据公告id删除公告
     *
     * @param announcementId
     */
    void deleteAnnouncementById(Integer announcementId);

    /**
     * 置顶或取消置顶公告
     *
     * @param announcementId
     * @param top
     */
    void updateAnnouncementTop(Integer announcementId, Integer top);

    /**
     * 获取公告数量
     * @return
     */
    Long getAnnouncementCount();

    /**
     * 分页查询公告
     * @param page
     * @param showCount
     * @return
     */
    List<Announcement> findAnnouncement(Integer page, Integer showCount);

}
