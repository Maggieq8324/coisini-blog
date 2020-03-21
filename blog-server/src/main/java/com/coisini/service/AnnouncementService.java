package com.coisini.service;

import com.coisini.mapper.AnnouncementMapper;
import com.coisini.entity.Announcement;
import com.coisini.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnnouncementService {
    @Autowired
    private AnnouncementMapper announcementMapper;

    @Autowired
    private DateUtil dateUtil;

    public void saveAnnouncement(String announcementTitle, String announcementBody) {
        announcementBody = announcementBody.replaceAll("\n", "<br />");
        Announcement announcement = new Announcement();
        announcement.setTitle(announcementTitle);
        announcement.setBody(announcementBody);
        announcement.setTime(dateUtil.getCurrentDate());
        announcement.setTop(1);
        announcementMapper.saveAnnouncement(announcement);
    }

    /**
     * 根据公告id删除公告
     *
     * @param announcementId
     */
    public void deleteAnnouncementById(Integer announcementId) {
    	announcementMapper.deleteAnnouncementById(announcementId);
    }


    /**
     * 置顶或取消置顶公告
     *
     * @param announcementId
     * @param top
     */
    public void updateAnnouncementTop(Integer announcementId, Integer top) {
        Announcement announcement = new Announcement();
        announcement.setId(announcementId);
        announcement.setTop(top);
        announcementMapper.updateAnnouncementTop(announcement);
    }


    /**
     * 获取公告数量
     *
     * @return
     */
    public Long getAnnouncementCount() {
        return announcementMapper.getAnnouncementCount();
    }

    /**
     * 分页查询公告
     *
     * @param page
     * @param showCount
     * @return
     */
    public List<Announcement> findAnnouncement(Integer page, Integer showCount) {
        return announcementMapper.findAnnouncement((page - 1) * showCount, showCount);
    }
}
