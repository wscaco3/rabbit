package com.rabbit.controller.common;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.name.Rename;
import com.rabbit.util.DateUtil;
import com.rabbit.util.FileUtil;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.upload.UploadFile;

public class UploadFileCtrl extends Controller {
	
	public void index(){
		String folder = "common/"+DateUtil.dateToStr(DateUtil.yyyyMM, new Date());
		UploadFile img = getFiles(folder).get(0);	
		String result = "/upload/"+folder+"/"+img.getFileName();
		renderHtml(result);
	}
	
	//截图图片保存
	public void croppicsave(){
		Map<String,Object> rmap = new HashMap<String,Object>();
		String folder = "crop/"+DateUtil.dateToStr(DateUtil.yyyyMM, new Date());
		UploadFile img = getFiles(folder).get(0);
		String url = "/upload/"+folder+"/"+img.getFileName();
		List<String> imgtypes = Arrays.asList("image/jpg","image/jpeg","image/png","image/gif");
		if(imgtypes.contains(img.getContentType().toLowerCase())){
			try {
				InputStream is = new FileInputStream(img.getFile());
				BufferedImage buffimage = ImageIO.read(is);
				rmap.put("status", "success");
				rmap.put("url",url);
				rmap.put("width",buffimage.getWidth());
				rmap.put("height",buffimage.getHeight());
			} catch (Exception e) {
				rmap.put("status", "error");
				rmap.put("message", "文件上传发生错误！");
				e.printStackTrace();
			}
		}
		else{
			FileUtil.delFile(PathKit.getWebRootPath()+url);
			rmap.put("status", "error");
			rmap.put("message", "文件类型有误，请上传图片文件！");
		}
		renderJson(rmap);
	}
	
	//截图图片处理(不包括旋转功能)
	public void croppiccrop(){
		Map<String,Object> rmap = new HashMap<String,Object>();
		rmap.put("status", "error");
		String folder = "crop/"+DateUtil.dateToStr(DateUtil.yyyyMM, new Date());
		if(StrKit.notBlank(getPara("imgUrl"))&&StrKit.notBlank(getPara("imgInitW"))&&StrKit.notBlank(getPara("imgInitH"))&&StrKit.notBlank(getPara("imgW"))
				&&StrKit.notBlank(getPara("imgH"))&&StrKit.notBlank(getPara("imgY1"))&&StrKit.notBlank(getPara("imgX1"))
				&&StrKit.notBlank(getPara("cropW"))&&StrKit.notBlank(getPara("cropH"))&&StrKit.notBlank(getPara("rotation"))){
			File desFolder = new File(PathKit.getWebRootPath()+"/upload/"+folder);
			try {
				Double scale = new Double(getPara("imgW"))/new Double(getPara("imgInitW"));
				Thumbnails.of(PathKit.getWebRootPath()+getPara("imgUrl")).scale(scale).sourceRegion((int)(new Double(getPara("imgX1"))/scale), (int)(new Double(getPara("imgY1")).intValue()/scale), (int)(new Double(getPara("cropW")).intValue()/scale), (int)(new Double(getPara("cropH")).intValue()/scale))
					.toFiles(desFolder, Rename.NO_CHANGE);
				rmap.put("status", "success");
				rmap.put("url",getPara("imgUrl")+"?t="+DateUtil.now());
			} catch (NumberFormatException | IOException e) {
				rmap.put("message", "图片裁剪发生错误！");
			};
		}
		else rmap.put("message", "参数有误！");
		renderJson(rmap);
	}
}