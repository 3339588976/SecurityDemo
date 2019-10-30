参考链接：https://blog.csdn.net/yuanlaijike/article/details/80249235
第一章
测试基于SpringBoot的安全框架Spring Security
实现步骤：
1.导入spring-security依赖
2.创建数据表（用户表，角色表，用户角色表）
3.创建登录页面和首页（static文件夹下）
注意：用户的登录认证是由Spring Security处理，请求路径默认为/login,用户名字段默认username,密码字段默认password
4.创建实体类/DAO/Service/Controller等
5.配置application.properties文件
6.配置SpringSecurity：自定义UserDetailService将用户信息和权限注入进去
7.启动-运行程序
注意：SprigBoot启动时会扫描@Configuration配置，加载配置类，但是配置类必须和启动类同包或者在其子包下
###############################################################################################
第二章
测试Spring Security的自动登录
1.修改login.html文件添加remember me
2.Cookie存储
在WebSecurityConfig的configure方法中添加rememberMe()
默认有效周期为2周
3.数据库存储
在客户端的Cookie中只保存一个无意义的加密串，在数据库中保存该加密串-用户信息的对应关系
自动登录时，用cookie中的加密串去数据库验证，如果通过，自动登录通过
1）创建一个token表存储信息
2）在WebSecurityConfig中注入datasource,创建PersistentTokenRepository的一个Bean
3）在configure方法中配置自动登录
选择自动登录后，Cookie和数据库都会存储token的信息
#############################################################################################
第三章
测试Spring Security处理异常
1.在WebSecurityConfig中指定错误的url
2.在Controller方法中处理异常信息
真正的login是由spring security处理的
#############################################################################################
第四章
测试Spring Security添加验证码
1.创建验证码Servlet
2.初始化验证图片信息
3.在启动类中注入该Servlet
4.在WebSecurityConfig中添加允许匿名访问的url
##############################################################################################
第五章
测试Spring Security的权限控制
1.对角色赋予权限，角色相同的用户，拥有相同的权限
2.创建权限表
3.创建POJO,Mapper,Service
4.自定义PermissionEvaluator重写hasPermission()
5.WebSecurityConfig中注册自定义权限评估类
思路如下：
通过Authentication获取登录用户的Role
遍历每一个Role,获取每个Role的所有Permission
遍历每一个Permission,判断该角色的url是否和传入的url相同/并且该角色的权限集包含访问的权限，返回true
如果没有找到，返回false
###############################################################################################
第六章
测试Spring Security的登录管理
1.自定义认证成功或失败的登录处理
去除WebSecurityConfig中登录成功的defaultSuccessUrl()和登录失败的failureUrl()配置
自定义登录成功或失败的处理类,实现认证成功或失败的接口
在WebSecurityConfig中注入自定义的处理类
2.Session登录超时
达到超时时间后，用户自动退出登录
配置文件中配置session过期时间
WebSecurityConfig中添加配置
Controller类中接口处理
3.最大登录数
限制单个用户能存在的最大session数量
修改WebSecurityConfig配置
自定义处理旧用户登录失败的的策略类
maxSessionsPreventsLogin(false):允许第二台机器登录，旧用户被踢出
maxSessionsPreventsLogin（true):不允许第二次登录
4.主动踢出用户
在WebSecurityConfig中注入SessionRegistry的Bean
在config方法中配置.sessionRegistry
Controller类接口处理
5.退出登录
使当前的Session失效
清空与当前用户有关的remember-me的记录
清空当前的Spring Context
重定向到登录页
6.Session共享（Redis方式实现）
1）配置Redis
2)配置Session共享
3）application中配置redis地址和session的存儲方式
4)启动类中添加@EnableRedisHttpSession
5)修改IDEA的配置，允许项目在多端口下启动
##################################################################
第七章
用户认证流程
登录请求 --> UsernamePasswordAuthenticationFilter --> (当前未认证)AuthenticationManager --> AuthenticationProvider --> UserDetailService --> UserDetail --> Authentication(已认证)
短信验证码登录
1.SmsCodeAuthenticationToken
2.SmsCodeAuthenticationFilter
3.SmsCodeAuthenticationProvider
4.登录成功或失败的处理类
5.每种登录方式创建一个配置文件，将自定义的处理类加入其中
6.将配置文件添加到WebSecurityConfig中
7.修改login.html，并添加短信验证码登录接口
















