# spring-cloud-config-study

### spring cloud config从3个维度管理配置文件：
- 服务器： 每个配置文件属于某一个服务器																	
- 工作环境：同一个配置文件可以按dev，prod, test等环境划分															
- 文件版本： 同一个服务器的不同实例可以使用不同版本的配置文件
    
### 简单工作流程	
- Client ————> Server ————> Local git repository ————> remote git repository		 
  - Client是基于spring-cloud-config-client jar的服务器
  - Server是一个独立的web应用，负责从git远程库拉取配置返回给客户端
  - Local Git repository：注意config server每次都会从远程库拉取文件到本地库，然后在返回给客户端
    
### 简单示例
#### config server
首先是Config-Server application.yml文件配置如下，web服务器端口8800，主要看下uri，这是我的远程库地址，将来会议ssh协议从远程库获取配置文件而且这里没有配置username以及password，			
所以本地与远程库需要配置ssh免密登录，远程库地址获取git remote -v
  
    server:
      port: 8800
    spring:
      application:
        name: configserver
      cloud:
        config:
          server:
            git:
              uri: git@github.com:Leeforrest/learn-git.git
配置文件写好之后，只需在启动类上加一个@EnableConfigServer就可以了，启动之后可在浏览器输入[http://localhost:8800/local/test/master](http://localhost:8800/local/test/master)查看返回json

    {
        "name": "local",
        "profiles": [
            "test"
        ],
        "label": "master",
        "version": "a514be6c10234b7d3332fd299b6ee9f54be93faa",
        "state": null,
        "propertySources": [
            {
                "name": "git@github.com:Leeforrest/learn-git.git/local-test.properties",
                "source": {
                    "testiflocalfirst": "if config server read local file first"
                }
            }
        ]
    }
    
- 这里需要讲下url的 local、test、master，local为各个服务器名字（我这里的叫local），test为运行环境（这里的运行环境是test）, master是git的master分支
  - 有个坑就是：Config-Server会把url拆分重组出一个文件名，如这里的文件名是local-test.properties，configserver将通知本地库以该文件名从远程库拉取文件
#### config client
看config-client配置，这里一定要注意一点，就是client的这些配置必须在bootstrap.yml文件中，否则，默认请求的是http://localhost:8888  
客户端用一个定时任务不断输出

    @Component
    @EnableScheduling
    public class Test {
      @Value("${testiflocalfirst}")
      private String gitRemoteContent;

      @Scheduled(initialDelayString = "1000", fixedRateString = "1000")
      private void sysout() {
          System.out.println("output :" + gitRemoteContent);
      }
    }
    
 输出结果：
 
     output :if config server read local file first
     output :if config server read local file first
