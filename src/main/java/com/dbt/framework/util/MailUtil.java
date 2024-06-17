 /**
  * 项目名称：V积分
  * 
  */
package com.dbt.framework.util;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.event.TransportEvent;
import javax.mail.event.TransportListener;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import com.dbt.vpointsshop.bean.VpsGiftCardQrcodeOrderInfo;
import com.dbt.vpointsshop.dto.VpsGiftCardQrcodeOrderInfoDTO;
import com.vjifen.server.base.datasource.redis.RedisUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.dbt.datasource.util.DbContextHolder;
import com.dbt.platform.activity.bean.VcodeQrcodeBatchInfo;
import com.dbt.platform.autocode.bean.VpsQrcodeOrder;


public class MailUtil {
	
	
//	private static String defaultSenderName = "";// 默认的发件人用户名，defaultEntity用得到
//	private static String defaultSenderPass = "";// 默认的发件人密码，defaultEntity用得到
//	private static String defaultSmtpHost = "";// 默认的邮件服务器地址，defaultEntity用得到
	private Logger log = Logger.getLogger(this.getClass());  
	private String smtpHost ; // 邮件服务器地址
	private String sendUserName; // 发件人的用户名
	private String sendUserPass; // 发件人密码

	private MimeMessage mimeMsg; // 邮件对象
	private Session session;
	private Properties props;
	private Multipart mp;// 附件添加的组件
	//private List<FileDataSource> files = new LinkedList<FileDataSource>();// 存放附件文件
	private static final String IMAGE_URL = PropertiesUtil.getPropertyValue("image_server_url");
	//"https://img.vjifen.com/images/vma/";//图片服务路径
	
	private static final String SMTP_HOST = "smtp.exmail.qq.com"; // 邮件服务器
	private static String EMAIL_NAME = "vmasc@vjifen.com";  // 公共邮箱账号
	private static String EMAIL_SECRET = "eg74be8hGX9bivDM"; //公共邮箱秘钥
	private static final String YWYPC = "一万一批次";
	// private final static String SERVER_PATH = "https://vct.vjifen.com/vjifenPlatform";
	

	
	/**
	 * 
	 * @param to  收件人邮箱地址
	 * @param cc  抄送的地址
	 * @param vpsQrcodeOrder 订单信息
	 * @param vpsVcodeFactory 工厂信息
	 * @param isSendFlag true:发送码包 false:发送密码
	 * @throws Exception
	 */
	
	public static void sendMail(String serverPath ,VpsQrcodeOrder vpsQrcodeOrder, boolean isSendFlag) throws Exception {
		String cc = "";
		String to = "";
		boolean isWanFlag = "1".equals(vpsQrcodeOrder.getIsWanBatch()) ? true : false;
		List<String> attachments = null;
		String subject = vpsQrcodeOrder.getProjectName() + (isWanFlag ? YWYPC + vpsQrcodeOrder.getOrderName() : vpsQrcodeOrder.getOrderName());
		subject = isSendFlag ? subject + "_码包" : subject + "_密码";
		StringBuffer content = null;
		if (isSendFlag) {
			to = vpsQrcodeOrder.getBagEmail();
			// 正文
			content = new StringBuffer("<html><head></head><body><br>" + vpsQrcodeOrder.getBagName() + "经理您好:</br><br>附件为"+ vpsQrcodeOrder.getOrderName() + "订单，请及时查收下载</br>");
			content.append("<table border=\"1\" width=100%  cellspacing=\"0\" style=\"border:solid 1px; text-align:center;\">");
			content.append("<tr><td>订单名称</td><td>" + vpsQrcodeOrder.getOrderName() + "</td></tr>"
						+ "<tr><td>客户订单号</td><td>" + vpsQrcodeOrder.getClientOrderNo() + "</td></tr>"	
						+ "<tr><td>下单日期</td><td>" + vpsQrcodeOrder.getOrderTime() + "</td></tr>" 
						+ "<tr><td>sku名称</td><td>" + vpsQrcodeOrder.getSkuName() + "</td></tr>" 
						+ "<tr><td>订单数量</td><td>" + vpsQrcodeOrder.getOrderQrcodeCount() + "个(含损耗)</td></tr>"
						+ "<tr><td>下单工厂</td><td>" + vpsQrcodeOrder.getBrewery()+ "</td></tr>"
						+ "<tr><td>赋码厂</td><td>" + vpsQrcodeOrder.getQrcodeManufacture() + "</td></tr>" 
						+ "<tr><td>需求省区</td><td>" + vpsQrcodeOrder.getProjectName() + "</td></tr>");
			String qrcodePackName = vpsQrcodeOrder.getOrderNo() + "_"+ vpsQrcodeOrder.getOrderName();
            String token = UUIDTools.getInstance().getUUID();
            RedisUtils.get().STRING.HASH.hSet(RedisApiUtil.CacheKey.codeToken.VJIFENCOM_TOKEN, qrcodePackName, token);
            String url = "vpsQrcodeOrder/downloadCode.do?orderKey=" + vpsQrcodeOrder.getOrderKey()
                    + "&projectServerName=" + DbContextHolder.getDBType() + "&orderNo=" + vpsQrcodeOrder.getOrderNo()
                    + "&orderName=" + vpsQrcodeOrder.getOrderName() + "&token=" + token;
			String filePath = "/upload/autocode" + "/" + vpsQrcodeOrder.getCreateTime().split(" ")[0].replaceAll("-", "") + "/"
					+ DbContextHolder.getDBType() + "/" + vpsQrcodeOrder.getOrderKey() + "/4compress/" + qrcodePackName;
			content.append("<tr><td>码包超链接</td><td>" + "<a href='" + serverPath + url + "'>码包连接地址</a></td></tr>");
			//拼接标签连接
            String qrcodePackLabelName = qrcodePackName + "_labeImage";
            String token1 = UUIDTools.getInstance().getUUID();
            RedisUtils.get().STRING.HASH.hSet(RedisApiUtil.CacheKey.codeToken.VJIFENCOM_TOKEN, qrcodePackLabelName, token1);
            String LabelUrl = "vpsQrcodeOrder/downloadCode.do?orderKey=" + vpsQrcodeOrder.getOrderKey()
                    + "&projectServerName=" + DbContextHolder.getDBType() + "&orderNo=" + vpsQrcodeOrder.getOrderNo()
                    + "&orderName=" + vpsQrcodeOrder.getOrderName() + "_labeImage&token=" + token1;
			if ("1".equals(vpsQrcodeOrder.getIsLabel())) {
				content.append("<tr><td>标签超链接</td><td>" + "<a href='" + serverPath + LabelUrl + "'>标签连接地址</a></td></tr>");
			}
			content.append("</table>");
			content.append("<br></br>");
			// 批次列表
			content.append("<table border=\"1\" width=100% cellspacing=\"0\" style=\"border:solid 1px; text-align:center;\">");
			content.append("<tr style=\"background-color: #428BCA; color:#ffffff\"><th>序号</th><th>编号</th><th>批次编号</th><th>批次名称</th><th>批次数量(含损耗)</th></tr>");		
			long qrcodeNum = 0;
			VcodeQrcodeBatchInfo vcodeQrcodeBatchInfo = null;
			for (int i = 0; i < vpsQrcodeOrder.getBatchInfoList().size(); i++) {	
				vcodeQrcodeBatchInfo = vpsQrcodeOrder.getBatchInfoList().get(i);
				String batchDesc = vcodeQrcodeBatchInfo.getBatchDesc().replace("_WAN", "");
				content.append("<tr><td>" + String.format("%03d", i+1) + "</td><td>" + batchDesc.substring(batchDesc.length()-3) + "</td><td>" + vcodeQrcodeBatchInfo.getBatchDesc().replace("_WAN", "") + "</td><td>"+ vcodeQrcodeBatchInfo.getBatchName() + "</td><td>" + vcodeQrcodeBatchInfo.getQrcodeAmounts()+ "</td></tr>");
				qrcodeNum += vcodeQrcodeBatchInfo.getQrcodeAmounts();
			}
			content.append("<tr><td></td><td></td><td></td><td>" + "合计" + "</td><td>" + qrcodeNum + "</td></tr></table>");
			// 如果附件不为空则去图片服务器路径下查找资源
			if (StringUtils.isNotBlank(vpsQrcodeOrder.getImageUrl())) {
				attachments = new ArrayList<>();
				for (String imageUrl : vpsQrcodeOrder.getImageUrl().split(",")) {
					attachments.add(IMAGE_URL  + imageUrl);//https://img.vjifen.com/images/vma/
				}
			}
			//发送密码
		} else {
			cc = vpsQrcodeOrder.getCsEmail();
			to = vpsQrcodeOrder.getPasswordEmail();
			content = new StringBuffer("<html><head></head><body>" + vpsQrcodeOrder.getPasswordName() + "经理您好:<br>邮件为"+ vpsQrcodeOrder.getOrderName() + "订单码包密码，请注意查收</br>");
			content.append("<table border=\"1\" width=50% he cellspacing=\"0\" style=\"border:solid 1px; text-align:center;font-size:18px;font-family:serif;\">");
			content.append(	"<tr><td>订单名称</td><td>" + vpsQrcodeOrder.getOrderName() + "</td></tr>"
							  + "<tr><td>客户订单号</td><td>" + vpsQrcodeOrder.getClientOrderNo() + "</td></tr>"
					          + "<tr><td>码包密码</td><td>" + vpsQrcodeOrder.getPackagePassword() + "</td></tr></table>");
		}
		// 公司签名
		content.append("<h3>顺祝商祺</h3><br>咨询电话:010-56016692</br>");
		content.append("<img src=\"https://img.vjifen.com/images/vma/test/20200902/shandongagt/cc.jpg\"/>");
		content.append("</body></html>");
		MailUtil email = MailUtil.entity(SMTP_HOST, EMAIL_NAME, EMAIL_SECRET, to, cc, subject, content.toString(),attachments,null);
		// 拿到发送结果
		email.send(cc);
	}

	public static void sendMail(VpsGiftCardQrcodeOrderInfoDTO vpsQrcodeOrder) throws Exception {
		String cc = "";
		String to = "";
		List<String> attachments = null;
		String subject = vpsQrcodeOrder.getOrderName();
		StringBuffer content = null;
		cc = vpsQrcodeOrder.getCsEmail();
		to = vpsQrcodeOrder.getPasswordEmail();
		content = new StringBuffer("<html><head></head><body>" + vpsQrcodeOrder.getPasswordEmailName() + " 经理您好:<br>邮件为"+ vpsQrcodeOrder.getOrderName() + "兑付卡订单码包密码，请注意查收</br>");
		content.append("<table border=\"1\" width=80% he cellspacing=\"0\" style=\"border:solid 1px;padding-left:5%; text-align:center;font-size:18px;font-family:serif;\">");
		content.append(	"<tr><td>兑付卡订单名称</td><td>" + vpsQrcodeOrder.getOrderName() + "</td></tr>"
				+ "<tr><td>兑付卡订单号</td><td>" + vpsQrcodeOrder.getQrcodeOrderKey() + "</td></tr>"
				+ "<tr><td>兑付卡名称</td><td>" + vpsQrcodeOrder.getGiftCardName() + "</td></tr>"
				+ "<tr><td>制卡人</td><td>" + vpsQrcodeOrder.getCardMaker() + "</td></tr>"
				+ "<tr><td>制卡人联系方式</td><td>" + vpsQrcodeOrder.getCardMakerPhone() + "</td></tr>"
				+ "<tr><td>码包密码</td><td>" + vpsQrcodeOrder.getPassword() + "</td></tr></table>");
		// 公司签名
		content.append("<h3>顺祝商祺</h3><br>咨询电话:010-56016692</br>");
		content.append("<img src=\"https://img.vjifen.com/images/vma/test/20200902/shandongagt/cc.jpg\"/>");
		content.append("</body></html>");
		MailUtil email = MailUtil.entity(SMTP_HOST, EMAIL_NAME, EMAIL_SECRET, to, cc, subject, content.toString(),attachments,null);
		// 拿到发送结果
		email.send(cc);
	}
	
	/**
	 * 发送excel邮件
	 * @param is
	 * @throws Exception
	 */
	public static void sendMail(InputStream is,String to,String cc,boolean flag) throws Exception {
		String subject = "V积分" + DateUtil.getDateTime(DateUtil.addDays(-1), DateUtil.DEFAULT_DATE_FORMAT) + (flag?"每日战报":"每日大奖明细");
		StringBuffer content = new StringBuffer();
		// 正文
		if (flag) {
			content = new StringBuffer("<html><head></head><body><br>诸位:</br><br>邮件附件为"
					+ DateUtil.getDateTime(DateUtil.addDays(-1), DateUtil.DEFAULT_DATE_FORMAT) + "日战报，请查收</br>");
		} else {
			content = new StringBuffer("<html><head></head><body><br></br><br>邮件附件为"
					+ DateUtil.getDateTime(DateUtil.addDays(-1), DateUtil.DEFAULT_DATE_FORMAT) + "日大奖明细，请查收</br>");
		}
		// 公司签名
		setGSImage(content);
		MailUtil email = MailUtil.entity(SMTP_HOST, EMAIL_NAME, EMAIL_SECRET, to, cc, subject, content.toString(),
				null,is);
		// 拿到发送结果
		email.send(cc);
	}
	
	
	
	
	private void init() {
		if (props == null) {
			props = System.getProperties();
		}
		props.put("mail.smtp.host", smtpHost);
		props.setProperty("mail.smtp.port", "587"); //qq
		props.put("mail.smtp.auth", "true"); // 需要身份验证
		session = Session.getDefaultInstance(props, null);
		// 置true可以在控制台（console)上看到发送邮件的过程
		session.setDebug(true);
		// 用session对象来创建并初始化邮件对象
		mimeMsg = new MimeMessage(session);
		// 生成附件组件的实例
		mp = new MimeMultipart();
	}
 
	private MailUtil(String smtpHost, String sendUserName, String sendUserPass, String to, String cc, String mailSubject, String mailBody,
			List<String> attachments,InputStream is) {
		this.smtpHost = smtpHost;
		this.sendUserName = sendUserName;
		this.sendUserPass = sendUserPass;
 
		init();
		setFrom(sendUserName);
		setTo(to);
		setCC(cc);
		setBody(mailBody);
		setSubject(mailSubject);
		if (attachments != null) {
			for (String attachment : attachments) {
				addFileAffix(attachment);
			}
		}
		if(is != null){
			addExcelffix(is, mailSubject);
		}
	}
 
	/**
	 * 邮件实体
	 * 
	 * @param smtpHost
	 *            邮件服务器地址
	 * @param sendUserName
	 *            发件邮件地址
	 * @param sendUserPass
	 *            发件邮箱密码
	 * @param to
	 *            收件人，多个邮箱地址以半角逗号分隔
	 * @param cc
	 *            抄送，多个邮箱地址以半角逗号分隔
	 * @param mailSubject
	 *            邮件主题
	 * @param mailBody
	 *            邮件正文
	 * @param attachmentPath
	 *            附件路径
	 * @return
	 */
	public static MailUtil entity(String smtpHost, String sendUserName, String sendUserPass, String to, String cc, String mailSubject, String mailBody,
			List<String> attachments,InputStream is) {
		return new MailUtil(smtpHost, sendUserName, sendUserPass, to, cc, mailSubject, mailBody, attachments,is);
	}

 
	/**
	 * 设置邮件主题
	 * 
	 * @param mailSubject
	 * @return
	 */
	private boolean setSubject(String mailSubject) {
		try {
			mimeMsg.setSubject(mailSubject);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
 
	/**
	 * 设置邮件内容,并设置其为文本格式或HTML文件格式，编码方式为UTF-8
	 * 
	 * @param mailBody
	 * @return
	 */
	private boolean setBody(String mailBody) {
		try {
			BodyPart bp = new MimeBodyPart();
			bp.setContent("<meta http-equiv=Content-Type content=text/html; charset=UTF-8>" + mailBody, "text/html;charset=UTF-8");
			// 在组件上添加邮件文本
			mp.addBodyPart(bp);
		} catch (Exception e) {
			System.err.println("设置邮件正文时发生错误！" + e);
			return false;
		}
		return true;
	}
 
	/**
	 * 添加一个附件
	 * 
	 * @param filename
	 *            邮件附件的地址，可支持网络和本地
	 * @return
	 */
	public boolean addFileAffix(String filename) {
		try {
			if (filename != null && filename.length() > 0) {		
				 URL url = new URL(filename);
			     DataHandler dataHandler = new DataHandler(url);
			     MimeBodyPart messageBodyPart3 = new MimeBodyPart();
			     messageBodyPart3.setDataHandler(dataHandler);
			     FileDataSource ds=new FileDataSource(filename);
			     messageBodyPart3.setFileName(ds.getName());
			     mp.addBodyPart(messageBodyPart3);// 添加附件
//				BodyPart bp = new MimeBodyPart();
//				FileDataSource fileds = new FileDataSource(filename);
//				bp.setDataHandler(new DataHandler(fileds));
//				bp.setFileName(MimeUtility.encodeText(fileds.getName(), "utf-8", null)); // 解决附件名称乱码
//				mp.addBodyPart(bp);// 添加附件
//				files.add(fileds);
			}
		} catch (Exception e) {
			System.err.println("增加邮件附件：" + filename + "发生错误！" + e);
			return false;
		}
		return true;
	}
	
	
	public boolean addExcelffix(InputStream is, String mailSubject) {
		try {
			// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
			Multipart multipart = new MimeMultipart();
			// 设置邮件的文本内容
			BodyPart contentPart = new MimeBodyPart();
			contentPart.setText("请查看附件");
			multipart.addBodyPart(contentPart);
			// 添加附件
			BodyPart messageBodyPart = new MimeBodyPart();
			DataSource source = new ByteArrayDataSource(is, "application/msexcel");
			// 添加附件的内容
			messageBodyPart.setDataHandler(new DataHandler(source));
			// 添加附件的标题
			// 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
			messageBodyPart.setFileName(MimeUtility.encodeText(mailSubject + ".xls"));
			mp.addBodyPart(messageBodyPart);// 添加附件
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
 
	/**
	 * 设置发件人地址
	 * 
	 * @param from
	 *            发件人地址
	 * @return
	 */
	private boolean setFrom(String from) {
		try {
			mimeMsg.setFrom(new InternetAddress(from));
		} catch (Exception e) {
			return false;
		}
		return true;
	}
 
	/**
	 * 设置收件人地址
	 * 
	 * @param to收件人的地址
	 * @return
	 */
	private boolean setTo(String to) {
		if (to == null)
			return false;
		try {
			mimeMsg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
		} catch (Exception e) {
			return false;
		}
		return true;
	}
 
	/**
	 * 设置抄送
	 * 
	 * @param cc
	 * @return
	 */
	private boolean setCC(String cc) {
		if (cc == null) {
			return false;
		}
		try {
			mimeMsg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(cc));
		} catch (Exception e) {
			return false;
		}
		return true;
	}
 
	/**
	 * 设置公司名称
	 * @param content
	 */
	private static void setGSImage(StringBuffer content){
		content.append("<h3>顺祝商祺</h3><br>咨询电话:010-56016692</br>");
		content.append("<img src=\"https://img.vjifen.com/images/vma/test/20200902/shandongagt/cc.jpg\"/>");
		content.append("</body></html>");
	}
	
	/**
	 * 发送邮件
	 * 
	 * @return
	 */
	public String send(String cc) throws Exception {
		final DeliveredStateFuture future = new DeliveredStateFuture();		
		mimeMsg.setContent(mp);
		mimeMsg.saveChanges();
		System.out.println("正在发送邮件....");
		Transport transport = session.getTransport("smtp");
		transport.addTransportListener(new TransportListener() {
			//代表成功
		    public void messageDelivered(TransportEvent arg0) {
		        future.setState(DeliveredState.MESSAGE_DELIVERED);
		    }
		    //失败
		    public void messageNotDelivered(TransportEvent arg0) {
		        future.setState(DeliveredState.MESSAGE_NOT_DELIVERED);
		    }
		    //一半发送成功
		    public void messagePartiallyDelivered(TransportEvent arg0) {
		        future.setState(DeliveredState.MESSAGE_PARTIALLY_DELIVERED);
		    }
		});
		// 连接邮件服务器并进行身份验证
		transport.connect(smtpHost, sendUserName, sendUserPass);
		// 发送邮件
		transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.TO));
		if (StringUtils.isNotBlank(cc))
			transport.sendMessage(mimeMsg, mimeMsg.getRecipients(Message.RecipientType.CC));	
		System.out.println("发送邮件成功！");
		transport.close();
		future.waitForReady();
		log.warn("发送状态:"+future.getState().toString());
		return future.getState().toString();
	}

	
	private enum DeliveredState {
		INITIAL, MESSAGE_DELIVERED, MESSAGE_NOT_DELIVERED, MESSAGE_PARTIALLY_DELIVERED,
	}

	private static class DeliveredStateFuture {
		private DeliveredState state = DeliveredState.INITIAL;
		synchronized void waitForReady() throws InterruptedException {
			if (state == DeliveredState.INITIAL) {
				wait();
			}
		}
		synchronized DeliveredState getState() {
			return state;
		}
		synchronized void setState(DeliveredState newState) {
			state = newState;
			notifyAll();
		}
	}
}
