#该镜像需要的基础镜像
FROM java:8
#将当前目录的jar复制到docker容器的/目录,根据情况改
ADD tian-search-1.0-SNAPSHOT.jar    /tian-search.jar
#运行过程中执行创建一个app.jar文件
RUN bash -c 'touch /tian-search.jar'
#声明服务暴露的端口
EXPOSE 3336
#指定docker运行的jar包
ENTRYPOINT ["java","-jar","/tian-search.jar"]
#指定维护者照片
MAINTAINER ctf
