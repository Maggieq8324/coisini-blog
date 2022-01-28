package com.coisini.service.impl;

import com.coisini.entity.Announcement;
import com.coisini.mapper.AnnouncementMapper;
import com.coisini.service.AnnouncementService;
import com.coisini.utils.DateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Description 公告实现类
 * @author coisini
 * @date Jan 19, 2022
 * @version 2.0
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementMapper announcementMapper;

    private final DateUtil dateUtil;

    /**
     * 保存公告
     * @param announcementTitle
     * @param announcementBody
     */
    @Override
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
     * @param announcementId
     */
    @Override
    public void deleteAnnouncementById(Integer announcementId) {
    	announcementMapper.deleteAnnouncementById(announcementId);
    }

    /**
     * 置顶或取消置顶公告
     * @param announcementId
     * @param top
     */
    @Override
    public void updateAnnouncementTop(Integer announcementId, Integer top) {
        Announcement announcement = new Announcement();
        announcement.setId(announcementId);
        announcement.setTop(top);
        announcementMapper.updateAnnouncementTop(announcement);
    }

    /**
     * 获取公告数量
     * @return
     */
    @Override
    public Long getAnnouncementCount() {
        return announcementMapper.getAnnouncementCount();
    }

    /**
     * 分页查询公告
     * @param page
     * @param showCount
     * @return
     */
    @Override
    public List<Announcement> findAnnouncement(Integer page, Integer showCount) {
        return announcementMapper.findAnnouncement((page - 1) * showCount, showCount);
    }
}
