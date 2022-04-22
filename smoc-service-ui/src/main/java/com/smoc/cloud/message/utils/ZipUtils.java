package com.smoc.cloud.message.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smoc.cloud.common.smoc.template.MessageFrameParamers;
import com.smoc.cloud.common.utils.DateTimeUtils;
import com.smoc.cloud.properties.MessageProperties;
import com.smoc.cloud.properties.ResourceProperties;
import org.apache.commons.io.FileUtils;
import org.apache.tools.zip.ZipFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component
public class ZipUtils {
    private static final int BUFFER_SIZE = 2 * 1024;

    @Autowired
    private MessageProperties messageProperties;

    @Autowired
    private ResourceProperties messageMmResourceProperties;


    /**
     * 根据json生成zip彩信包
     *
     * @param mulContentJson           彩信包内容
     * @param title           彩信模版标题，为空则设为当前压缩包名
     * @throws Exception 压缩失败会抛出运行时异常
     */
    /*public String jsonToZip(String mulContentJson, String title) throws Exception {
        //当前日期，uuid,用于生成文件夹路径
        String nowDay = DateTimeUtils.currentDate(new Date());
        String uuid = com.smoc.cloud.common.utils.UUID.uuid32();

        String zipTargetFolder = messageProperties.getMobileZipRootPath() + "/forInterface/" + nowDay + "/" + uuid;
        String zipTargetPath = zipTargetFolder+".zip";

        if(StringUtils.isEmpty(title)){
            title = uuid;
        }

        if(!StringUtils.isEmpty(mulContentJson)){
            List<MessageFrameParamers> paramsSort = new Gson().fromJson(mulContentJson, new TypeToken<List<MessageFrameParamers>>() {}.getType());

            //生成彩信包文件夹
            File zipFolder = new File(zipTargetFolder);
            zipFolder.mkdirs();

            //按次序将资源文件或者文本内容，拷贝或生成到彩信包文件夹内，同时生成SmilBodyPar数组，为创建smil文件做准备
            int indexInZip = 1;
            List<SmilBodyPar> pars = new ArrayList<SmilBodyPar>();
            for(MessageFrameParamers p:paramsSort){
                SmilBodyParImg smilBodyParImg = null;
                SmilBodyParTxt smilBodyParTxt = null;

                String resType = p.getResType();
                //图片等多媒体文件拷贝
                if(!"0".equals(resType)){
                    String sourcePath = messageProperties.getMobileZipRootPath() + p.getResUrl();
                    String suffixName = p.getResUrl().split("\\.")[1];
                    String targerPath = zipTargetFolder + "/" + indexInZip + "." + suffixName;
                    FileUtils.copyFile(new File(sourcePath), new File(targerPath));

                    smilBodyParImg = new SmilBodyParImg();
                    smilBodyParImg.setRegion("Image");
                    smilBodyParImg.setSrc(indexInZip+"."+suffixName);

                    indexInZip++;
                }

                //根据文本内容生成txt文件
                if(!StringUtils.isEmpty(p.getFrameTxt())){
                    String targerPath = zipTargetFolder + "/" + indexInZip + ".txt";
                    FileUtils.writeStringToFile(new File(targerPath), p.getFrameTxt());

                    smilBodyParTxt = new SmilBodyParTxt();
                    smilBodyParTxt.setRegion("Text");
                    smilBodyParTxt.setSrc(indexInZip+".txt");

                    indexInZip++;
                }

                //设置帧停留属性值
                SmilBodyPar par = new SmilBodyPar(smilBodyParImg, smilBodyParTxt);
                par.setDur(p.getStayTimes()+"000ms");
                pars.add(par);
            }

            //生成彩信包标题文件
            String targerPath_title = zipTargetFolder + "/title.txt";
            FileUtils.writeStringToFile(new File(targerPath_title), title);

            //生成彩信包smil文件
            String targerPath_mms = zipTargetFolder + "/mms.smil";
            FileUtils.writeStringToFile(new File(targerPath_mms), formatSmil(pars));

            //生成彩信压缩包
            ZipUtils.toZip(zipTargetFolder, zipTargetPath, false);

            //彩信压缩包生成后，删除彩信包文件夹
            FileUtils.deleteQuietly(zipFolder);
        }

        return zipTargetPath;
    }*/

    /**
     * 构建smil对象并转成XML字符串
     */
    private String formatSmil(List<SmilBodyPar> pars) throws Exception {
        SmilHeadLayoutRootLayoutHeadRegion r1 = new SmilHeadLayoutRootLayoutHeadRegion();
        r1.setId("Image");
        r1.setTop("0");
        r1.setLeft("0");
        r1.setHeight("50");
        r1.setWidth("100");
        r1.setFit("hidden");
        SmilHeadLayoutRootLayoutHeadRegion r2 = new SmilHeadLayoutRootLayoutHeadRegion();
        r2.setId("Text");
        r2.setTop("50");
        r2.setLeft("0");
        r2.setHeight("50");
        r2.setWidth("100");
        r2.setFit("hidden");
        List<SmilHeadLayoutRootLayoutHeadRegion> regions = new ArrayList<SmilHeadLayoutRootLayoutHeadRegion>();
        regions.add(r1);
        regions.add(r2);

        SmilHeadLayoutRootLayout rootLayout = new SmilHeadLayoutRootLayout(regions);
        rootLayout.setHeight("208");
        rootLayout.setWidth("176");

        SmilHeadLayout headLayout = new SmilHeadLayout(rootLayout);
        SmilHead smilHead = new SmilHead(headLayout);

        SmilBody body = new SmilBody(pars);
        Smil smilInfo = new Smil(smilHead, body);
        smilInfo.setXmlns("http://www.w3.org/2000/SMIL20/CR/Language");
        return XMLUtils.convertToXml(smilInfo);
    }


    /**
     * 解压zip彩信包生成彩信包内容jsonInfo
     *
     * @param zipFilePath           彩信zip包文件路径
     * @throws Exception 操作失败会抛出运行时异常
     */
   /* public String zipToJson(String zipFilePath) throws Exception {
        String mulContentJson = null;

        if(!StringUtils.isEmpty(zipFilePath)){
            JSONArray zipJsonContent = new JSONArray();

            //当前日期，uuid,用于生成文件夹路径
            String nowDay = DateTimeUtils.currentDate(new Date());
            String uuid = com.smoc.cloud.common.utils.UUID.uuid32();

            String unzipTargetFolderPath = "/forInterface/" + nowDay + "/" + uuid;
            unZipFiles(zipFilePath,  messageProperties.getMobileZipRootPath() + unzipTargetFolderPath);

            File smil = new File(messageProperties.getMobileZipRootPath() + unzipTargetFolderPath+"/mms.smil");
            String xmlContent = FileUtils.readFileToString(smil);
            xmlContent = xmlContent.replaceAll(" xmlns=\"http://www.w3.org/2000/SMIL20/CR/Language\"", "");
            Smil smilInfo = (Smil)XMLUtils.unmarshaller(Smil.class, xmlContent);
            SmilBody smilBody = smilInfo.getBody();
            List<SmilBodyPar> parList = smilBody.getParList();
            for(int i=0;i<parList.size();i++){
                SmilBodyPar par = parList.get(i);

                JSONObject tmpJson = new JSONObject();
                tmpJson.put("index", i+1);
                tmpJson.put("resId", "");
                tmpJson.put("resTitle", "");
                String dur = par.getDur();
                dur = dur.replaceAll("ms", "");
                Integer stayTimes = new Integer(dur)/1000;
                tmpJson.put("stayTimes", stayTimes.toString());

                if(par.getSmilBodyParImg()==null){
                    tmpJson.put("resSize", "0");
                    tmpJson.put("showUrl", "");
                    tmpJson.put("resUrl", "");
                    tmpJson.put("resType", "0");
                    tmpJson.put("resPostfix", "");

                    File tmp = new File(messageProperties.getMobileZipRootPath() + unzipTargetFolderPath+"/"+par.getSmilBodyParTxt().getSrc());
                    tmpJson.put("frameTxt", FileUtils.readFileToString(tmp));
                }else{
                    String imgSrc = par.getSmilBodyParImg().getSrc();
                    String resPostfixTmp = imgSrc.split("\\.")[1];
                    String showUrl = messageProperties.getMobileZipRootPath() + unzipTargetFolderPath+"/"+par.getSmilBodyParImg().getSrc();
                    File imgSrcFile = new File(showUrl);

                    tmpJson.put("resUrl",  unzipTargetFolderPath+"/"+imgSrc);
                    tmpJson.put("showUrl", showUrl);
                    tmpJson.put("resSize", new Long(imgSrcFile.length()/1024).toString());
                    tmpJson.put("resPostfix", resPostfixTmp);

                    if(messageMmResourceProperties.getResourceAllowFormat()[0].indexOf(resPostfixTmp)!=-1) {
                        tmpJson.put("resType", "1");
                    }else if(messageMmResourceProperties.getResourceAllowFormat()[1].indexOf(resPostfixTmp)!=-1) {
                        tmpJson.put("resType", "2");
                    }else if(messageMmResourceProperties.getResourceAllowFormat()[2].indexOf(resPostfixTmp)!=-1) {
                        tmpJson.put("resType", "3");
                    }

                    if(par.getSmilBodyParTxt()!=null){
                        File tmp = new File(messageProperties.getMobileZipRootPath() + unzipTargetFolderPath+"/"+par.getSmilBodyParTxt().getSrc());
                        tmpJson.put("frameTxt", FileUtils.readFileToString(tmp));
                    }
                }

                zipJsonContent.add(tmpJson);
            }

            mulContentJson = zipJsonContent.toJSONString();
        }

        return mulContentJson;
    }
*/

    /**
     * 压缩成ZIP 单文件压缩
     *
     * @param srcDir           压缩文件夹路径
     * @param outDir           压缩文件输出路径
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception 压缩失败会抛出运行时异常
     */
    public static void toZip(String srcDir, String outDir, boolean KeepDirStructure) throws Exception {
        FileOutputStream fout = new FileOutputStream(outDir);
        ZipOutputStream zos = new ZipOutputStream(fout);
        File sourceFile = new File(srcDir);
        compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
        zos.close();
    }


    /**
     * 压缩成ZIP 多文件压缩
     *
     * @param srcFiles 需要压缩的文件列表
     * @param outDir   压缩文件输出路径
     * @throws Exception 压缩失败会抛出运行时异常
     */
    public static void toZip(List<File> srcFiles, String outDir) throws Exception {
        FileOutputStream fout = new FileOutputStream(outDir);
        ZipOutputStream zos = new ZipOutputStream(fout);
        for (File srcFile : srcFiles) {
            byte[] buf = new byte[BUFFER_SIZE];
            zos.putNextEntry(new ZipEntry(srcFile.getName()));
            int len;
            FileInputStream in = new FileInputStream(srcFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            in.close();
        }
        zos.close();
    }


    /**
     * 递归压缩方法
     *
     * @param sourceFile       源文件
     * @param zos              zip输出流
     * @param name             压缩后的名称
     * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     */
    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }

            }
        }
    }

    /**
     * 解压zip格式的压缩文件到指定位置
     *
     * @param zipFileName 压缩文件
     * @param extPlace    解压目录
     * @throws Exception
     */
    public static void unZipFiles(String zipFileName, String extPlace) throws Exception {
        System.setProperty("sun.zip.encoding",
        System.getProperty("sun.jnu.encoding"));

        (new File(extPlace)).mkdirs();
        File f = new File(zipFileName);
        ZipFile zipFile = new ZipFile(zipFileName, "GBK"); // 处理中文文件名乱码的问题
        if ((!f.exists()) && (f.length() <= 0)) {
            throw new Exception("要解压的文件不存在!");
        }
        String strPath, gbkPath, strtemp;
        File tempFile = new File(extPlace);
        strPath = tempFile.getAbsolutePath();
        Enumeration<?> e = zipFile.getEntries();
        while (e.hasMoreElements()) {
            org.apache.tools.zip.ZipEntry zipEnt = (org.apache.tools.zip.ZipEntry) e.nextElement();
            gbkPath = zipEnt.getName();
            if (zipEnt.isDirectory()) {
                strtemp = strPath + "/" + gbkPath;
                File dir = new File(strtemp);
                dir.mkdirs();
                continue;
            } else { // 读写文件
                InputStream is = zipFile.getInputStream(zipEnt);
                BufferedInputStream bis = new BufferedInputStream(is);
                gbkPath = zipEnt.getName();
                strtemp = strPath + "/" + gbkPath;// 建目录
                String strsubdir = gbkPath;
                for (int i = 0; i < strsubdir.length(); i++) {
                    if (strsubdir.substring(i, i + 1).equalsIgnoreCase("/")) {
                        String temp = strPath + "/" + strsubdir.substring(0, i);
                        File subdir = new File(temp);
                        if (!subdir.exists())
                            subdir.mkdir();
                    }
                }
                FileOutputStream fos = new FileOutputStream(strtemp);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                int c;
                while ((c = bis.read()) != -1) {
                    bos.write((byte) c);
                }
                bos.close();
                fos.close();
            }
        }
    }


//    public static void main(String[] args) throws Exception {
//        toZip("C:\\Users\\dev-1\\Desktop\\meip\\普通彩信包\\", "C:\\Users\\dev-1\\Desktop\\meip\\p.zip", false);
//        unZipFiles("C:\\Users\\dev-1\\Desktop\\meip\\p.zip", "C:\\Users\\dev-1\\Desktop\\meip\\p");
//    }
}