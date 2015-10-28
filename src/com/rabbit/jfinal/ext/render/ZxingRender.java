package com.rabbit.jfinal.ext.render;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Hashtable;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.jfinal.render.Render;

public class ZxingRender extends Render {

	
	private String text;
	private int img_width = 300;
	private int img_height = 300;	
	private int margin = 1;	
	private int frontColor = 0xFF000000;
	private int backColor = 0xFFFFFFFF;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getImg_width() {
		return img_width;
	}

	public void setImg_width(int img_width) {
		this.img_width = img_width;
	}

	public int getImg_height() {
		return img_height;
	}

	public void setImg_height(int img_height) {
		this.img_height = img_height;
	}

	public int getMargin() {
		return margin;
	}

	public void setMargin(int margin) {
		this.margin = margin;
	}

	public int getFrontColor() {
		return frontColor;
	}

	public void setFrontColor(int frontColor) {
		this.frontColor = frontColor;
	}

	public int getBackColor() {
		return backColor;
	}

	public ZxingRender(String text, int img_width, int img_height) {
		super();
		this.text = text;
		this.img_width = img_width;
		this.img_height = img_height;
	}
	
	public ZxingRender(String text, int img_width, int img_height,int margin) {
		super();
		this.text = text;
		this.img_width = img_width;
		this.img_height = img_height;
		this.margin = margin;
	}
	
	public ZxingRender(String text) {
		super();
		this.text = text;
	}

	public void setBackColor(int backColor) {
		this.backColor = backColor;
	}

	@Override
	public void render() {
		Hashtable<EncodeHintType,Object> hints = new Hashtable<EncodeHintType,Object>();
		// 内容所使用编码
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, margin);
		BitMatrix bitMatrix;
		BufferedImage image = null;
		try {
			bitMatrix = new MultiFormatWriter().encode(text,
					BarcodeFormat.QR_CODE, img_width, img_height, hints);
			image = new BufferedImage(img_width, img_height,
					BufferedImage.TYPE_INT_RGB);
			for (int x = 0; x < img_width; x++) {
				for (int y = 0; y < img_height; y++) {
					image.setRGB(x, y, bitMatrix.get(x, y) ? frontColor : backColor);
				}
			}
		} catch (WriterException e1) {
		}
		response.setHeader("Pragma", "no-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/jpeg");

		ServletOutputStream sos = null;
		try {
			sos = response.getOutputStream();
			ImageIO.write(image, "jpeg", sos);
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (sos != null)
				try {
					sos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

}
