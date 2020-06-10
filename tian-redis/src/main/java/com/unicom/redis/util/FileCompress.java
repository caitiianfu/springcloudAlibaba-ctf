package com.unicom.redis.util;

/**
 * @author by ctf
 * @Classsname FileCompress
 * @Description TODO
 * @Date 2020/6/1 23:01
 **/

import com.google.common.primitives.Bytes;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @program: springBootPractice
 * @description: 文件压缩
 * @author: hu_pf
 * @create: 2019-08-14 10:15
 **/
//@OutputTimeUnit(TimeUnit.SECONDS)
//@BenchmarkMode({Mode.AverageTime})
public class FileCompress {

    //要压缩的图片文件所在所存放位置
    public static String JPG_FILE_PATH = "G:\\img\\333.jpg";

    //zip压缩包所存放的位置
    public static String ZIP_FILE = "G:\\img\\hu.zip";

    //所要压缩的文件
    public static File JPG_FILE = null;

    //文件大小
    public static long FILE_SIZE = 0;

    //文件名
    public static String FILE_NAME = "";

    //文件后缀名
    public static String SUFFIX_FILE = "";

    private static CountDownLatch countDownLatch=new CountDownLatch(1);
    static {

        File file = new File(JPG_FILE_PATH);

        JPG_FILE = file;

        FILE_NAME = file.getName();

        FILE_SIZE = file.length();

        SUFFIX_FILE = file.getName().substring(file.getName().indexOf('.'));
    }

    public static void main(String[] args) throws IOException {

//        Options opt = new OptionsBuilder()
//                .include(FileCompress.class.getSimpleName())
//                .forks(1).warmupIterations(10).threads(1)
//                .build();
//        new Runner(opt).run();

//        System.out.println("------NoBuffer");
   //     zipFileNoBuffer();

//        System.out.println("------Buffer");
   //     zipFileBuffer();  13600   不可浏览

//        System.out.println("------Channel");
  //    zipFileChannel(); // 12414

//        System.out.println("---------Map");
   //     zipFileMap();  // 12782

//        System.out.println("-------Pip");
   //   zipFilePip(); // 17696
   //     test();   13618


        //        System.out.println("-------buffer  byte read");
        //buffByte();



    }
    public static  void buffByte() throws IOException {
        long beginTime=System.currentTimeMillis();

        File file=new File(ZIP_FILE);
        BufferedOutputStream outputStream=new BufferedOutputStream(new FileOutputStream(file));
        ZipOutputStream zipOutputStream=new ZipOutputStream(outputStream);
       for (int i=0;i<11;i++){
           BufferedInputStream inputStream=new BufferedInputStream(new FileInputStream(JPG_FILE));
           byte[] buffer=new byte[1024];
           zipOutputStream.putNextEntry(new ZipEntry(i+FILE_NAME));
           int len;
           while ((len=inputStream.read(buffer))>0){
               zipOutputStream.write(buffer,0,len);
           }

           zipOutputStream.closeEntry();
           inputStream.close();
       }

zipOutputStream.close();
        printInfo(beginTime);

    }



    //Version 1 没有使用Buffer
    public static void zipFileNoBuffer() throws IOException {
        File zipFile = new File(ZIP_FILE);
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile))) {
            //开始时间
            long beginTime = System.currentTimeMillis();

            for (int i = 0; i < 10; i++) {
                try (InputStream input = new FileInputStream(JPG_FILE)) {
                    zipOut.putNextEntry(new ZipEntry(FILE_NAME + i));
                    int temp = 0;
                    while ((temp = input.read()) != -1) {
                        zipOut.write(temp);
                    }
                }
            }
            printInfo(beginTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Version 2 使用了Buffer
//    @Benchmark
    public static void zipFileBuffer() {
        //开始时间
        long beginTime = System.currentTimeMillis();
        File zipFile = new File(ZIP_FILE);
        try {
            ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(zipOut);
            for (int i = 0; i < 10; i++) {
                BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(JPG_FILE));
                zipOut.putNextEntry(new ZipEntry(i + SUFFIX_FILE));
                int temp = 0;
                while ((temp = bufferedInputStream.read()) != -1) {
                    bufferedOutputStream.write(temp);
                }
                zipOut.closeEntry();
                bufferedInputStream.close();
            }
            zipOut.close();
            printInfo(beginTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void test(){
        long beginTime=System.currentTimeMillis();
        FileOutputStream fos = null;
        ZipOutputStream zos = null;
        try {
            byte[] buffer = new byte[1024];

            fos = new FileOutputStream(ZIP_FILE);

            zos = new ZipOutputStream(fos);
            for (int i=0; i < 10; i++) {
                File srcFile = JPG_FILE;
                FileInputStream fis = new FileInputStream(srcFile);
                zos.putNextEntry(new ZipEntry(i + SUFFIX_FILE));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
//            zos.close();
        }
        catch (IOException ioe) {
            System.out.println("Error creating zip file: " + ioe);
        } finally {
            try {
                zos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            printInfo(beginTime);

        }
    }

    //    @Benchmark
    public static void zipFileChannelBuffer() {
        //开始时间
        long beginTime = System.currentTimeMillis();
        File zipFile = new File(ZIP_FILE);
        try (ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
             WritableByteChannel writableByteChannel = Channels.newChannel(zipOut)) {
            for (int i = 0; i < 10; i++) {
                try (FileChannel fileChannel = new FileInputStream(JPG_FILE).getChannel()) {
                    zipOut.putNextEntry(new ZipEntry(i + SUFFIX_FILE));
                    fileChannel.transferTo(0, FILE_SIZE, writableByteChannel);
                }
            }
            printInfo(beginTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Version 3 使用Channel
    public static void zipFileChannel() {
        //开始时间
        long beginTime = System.currentTimeMillis();
        File zipFile = new File(ZIP_FILE);
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
             WritableByteChannel writableByteChannel = Channels.newChannel(zipOut)) {
            for (int i = 0; i < 10; i++) {
                try (FileChannel fileChannel = new FileInputStream(JPG_FILE).getChannel()) {
                    zipOut.putNextEntry(new ZipEntry(i + SUFFIX_FILE));
                    fileChannel.transferTo(0, FILE_SIZE, writableByteChannel);
                }
            }
            printInfo(beginTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void zipFileMapBuffer() {
        //开始时间
        long beginTime = System.currentTimeMillis();
        File zipFile = new File(ZIP_FILE);
        try (ZipOutputStream zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)));
             WritableByteChannel writableByteChannel = Channels.newChannel(zipOut)) {
            for (int i = 0; i < 10; i++) {
                zipOut.putNextEntry(new ZipEntry(i + SUFFIX_FILE));

                //内存中的映射文件
                MappedByteBuffer mappedByteBuffer = new RandomAccessFile(JPG_FILE_PATH, "r").getChannel()
                        .map(FileChannel.MapMode.READ_ONLY, 0, FILE_SIZE);

                writableByteChannel.write(mappedByteBuffer);
            }
            printInfo(beginTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //Version 4 使用Map映射文件
    public static void zipFileMap() {
        //开始时间
        long beginTime = System.currentTimeMillis();
        File zipFile = new File(ZIP_FILE);
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
             WritableByteChannel writableByteChannel = Channels.newChannel(zipOut)) {
            for (int i = 0; i < 10; i++) {
                zipOut.putNextEntry(new ZipEntry(i + SUFFIX_FILE));

                //内存中的映射文件
                MappedByteBuffer mappedByteBuffer = new RandomAccessFile(JPG_FILE_PATH, "r").getChannel()
                        .map(FileChannel.MapMode.READ_ONLY, 0, FILE_SIZE);

                writableByteChannel.write(mappedByteBuffer);
            }
            printInfo(beginTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    //Version 5 使用Pip
//    @Benchmark
    public static void zipFilePip() {

        long beginTime = System.currentTimeMillis();
        try(WritableByteChannel out = Channels.newChannel(new FileOutputStream(ZIP_FILE))) {
            Pipe pipe = Pipe.open();
            //异步任务
            CompletableFuture.runAsync(()->runTask(pipe));

            //获取读通道
            ReadableByteChannel readableByteChannel = pipe.source();
            ByteBuffer buffer = ByteBuffer.allocate(((int) FILE_SIZE)*10);
            while (readableByteChannel.read(buffer)>= 0) {
                buffer.flip();
                out.write(buffer);
                buffer.clear();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        printInfo(beginTime);

    }

    //异步任务
    public static void runTask(Pipe pipe) {

        try(ZipOutputStream zos = new ZipOutputStream(Channels.newOutputStream(pipe.sink()));
            WritableByteChannel out = Channels.newChannel(zos)) {
            System.out.println("Begin");
            for (int i = 0; i < 10; i++) {
                zos.putNextEntry(new ZipEntry(i+SUFFIX_FILE));

                FileChannel jpgChannel = new FileInputStream(new File(JPG_FILE_PATH)).getChannel();

                jpgChannel.transferTo(0, FILE_SIZE, out);

                jpgChannel.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }



    private static void printInfo(long beginTime) {
        //耗时
        long timeConsum = (System.currentTimeMillis() - beginTime);

        System.out.println("fileSize:" + FILE_SIZE / 1024 / 1024 * 10 + "M");
        System.out.println("consum time:" + timeConsum);
    }



    //Version 6 使用Pip+Map
//    @Benchmark
    public static void zipFilePipMap() {
        long beginTime = System.currentTimeMillis();
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(ZIP_FILE));
             WritableByteChannel out = Channels.newChannel(fileOutputStream)) {
            Pipe pipe = Pipe.open();
            //异步任务往通道中塞入数据
            CompletableFuture.runAsync(() -> runTaskMap(pipe));
            //读取数据
            ReadableByteChannel workerChannel = pipe.source();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while (workerChannel.read(buffer) >= 0) {
                buffer.flip();
                out.write(buffer);
                buffer.clear();
            }
            printInfo(beginTime);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //异步任务
    public static void runTaskMap(Pipe pipe) {

        try (WritableByteChannel channel = pipe.sink();
             ZipOutputStream zos = new ZipOutputStream(Channels.newOutputStream(channel));
             WritableByteChannel out = Channels.newChannel(zos)) {
            for (int i = 0; i < 10; i++) {
                zos.putNextEntry(new ZipEntry(i + SUFFIX_FILE));

                MappedByteBuffer mappedByteBuffer = new RandomAccessFile(
                        JPG_FILE_PATH, "r").getChannel()
                        .map(FileChannel.MapMode.READ_ONLY, 0, FILE_SIZE);
                out.write(mappedByteBuffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
