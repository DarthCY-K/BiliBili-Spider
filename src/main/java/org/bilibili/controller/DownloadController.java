package org.bilibili.controller;

import org.bilibili.mappers.VideoMapper;
import org.bilibili.service.DownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author DarthCY
 */
@Controller
public class DownloadController {
    @Autowired
    private VideoMapper videoMapper;

    /**
     * 根据给定bv号下载视频
     *
     * @param bId 视频所在数据库记录的id
     * @return 是否成功
     */
    @RequestMapping("/downloadVideoFile")
    @ResponseBody
    public boolean downloadVideoFile(@RequestParam(defaultValue="")String bId) {
        DownloadService.execute(videoMapper.searchVideoBid(bId));
        return true;
    }


}
