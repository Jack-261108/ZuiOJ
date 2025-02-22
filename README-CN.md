# README-CN
旅游几天
# OJ系统

 

## 环境准备

​	运行此程序必须要有**Ubuntu**系统，

​				Linux 内核版本 >= 3.10
​				jdk1.8版本，

​				mysql8.0，

​				redis。

### go-judge系统运行

**gojudge所需要的端口是5050**

<a href="">go_judge手册</a>.

 1.下载二进制预编译文件

https://github.com/criyle/go-judge/releases 

或者在Docker上运行

~~~bash
docker run -it --rm --privileged --shm-size=256m -p 5050:5050 criyle/executorserver
~~~

#### c/c++语言环境测试

**接下来我们打开postman进行测试已post形式发送请求http://IP:5050/run携带参数**

~~~java
{
    "cmd": [{
        "args": ["/usr/bin/g++", "Main.cc", "-o", "a"],
        "env": ["PATH=/usr/bin:/bin"],
        "files": [{
            "content": ""
        }, {
            "name": "stdout",
            "max": 10240
        }, {
            "name": "stderr",
            "max": 10240
        }],
        "cpuLimit": 10000000000,
        "memoryLimit": 104857600,
        "procLimit": 50,
        "copyIn": {
            "Main.cc": {
                "content": "#include <iostream>\nusing namespace std;\nint main() {\nint a, b;\ncin >> a >> b;\ncout << a + b << endl;\n}"
            }
        },
        "copyOut": ["stdout", "stderr"],
        "copyOutCached": ["Main.cc", "a"],
        "copyOutDir": "1"
    }]
}
~~~

**我们服务器会给我们返回**

~~~bash
[
	{
		"status": "Accepted",
		"exitStatus": 0,
		"time": 726910000,
		"memory": 55812096,
		"runTime": 787566071,
		"files": {
			"stderr": "",
			"stdout": ""
		},
		"fileIds": {
			"Main.cc": "4EK46KIB",
			"a": "LR567VHA"
		}
	}
]
~~~

**接下来我们根据Main的Id对他进行run**

~~~bash
{
    "cmd": [{
        "args": ["a"],
        "env": ["PATH=/usr/bin:/bin"],
        "files": [{
            "content": "1 1"
        }, {
            "name": "stdout",
            "max": 10240
        }, {
            "name": "stderr",
            "max": 10240
        }],
        "cpuLimit": 10000000000,
        "memoryLimit": 104857600,
        "procLimit": 50,
        "strictMemoryLimit": false,
        "copyIn": {
            "a": {
                "fileId": "LR567VHA"
            }
        }
    }]
}
~~~

**服务器对我们的反馈**

~~~bash
[
	{
		"status": "Accepted",
		"exitStatus": 0,
		"time": 7191000,
		"memory": 6672384,
		"runTime": 15512983,
		"files": {
			"stderr": "",
			"stdout": "2\n"
		}
	}
]
~~~

#### java语言环境测试

**接下来我们打开postman进行测试已post形式发送请求http://IP:5050/run携带参数**

~~~json
{
    "cmd": [{
        "args": ["/usr/bin/javac","-encoding","utf-8","Main.java"],
        "env": ["PATH=/usr/bin:/bin"],
        "files": [{
            "content": ""
        }, {
            "name": "stdout",
            "max": 102000
        }, {
            "name": "stderr",
            "max": 102400
        }],
        "cpuLimit": 10000000000,
        "memoryLimit": 104857600,
        "procLimit": 50,
        "copyIn": {
            "Main.java": {
                "content": "public class Main{  public static void main(String[] args){    System.out.println(\"hello world\");  }}"
            }
        },
        "copyOut": ["stdout", "stderr"],
        "copyOutCached": ["Main.java", "Main.class"],
    }]
}
~~~

**服务器反馈给我们的**

~~~java
[
	{
		"status": "Accepted",
		"exitStatus": 0,
		"time": 1355176000,
		"memory": 77733888,
		"runTime": 613378383,
		"files": {
			"stderr": "",
			"stdout": ""
		},
		"fileIds": {
			"Main.class": "WD6HUHUY",
			"Main.java": "HFVY5JXI"
		}
	}
]
~~~

**接下来我们根据Main的Id对他进行run**

~~~bash
{
    "cmd": [{
        "args": ["/usr/bin/java","Main"],
        "env": ["PATH=/usr/bin:/bin"],
        "files": [{
            "content": "1 1"
        }, {
            "name": "stdout",
            "max": 10240
        }, {
            "name": "stderr",
            "max": 10240
        }],
        "cpuLimit": 10000000000,
        "memoryLimit": 104857600,
        "procLimit": 50,
        "strictMemoryLimit": false,
        "copyIn": {
            "Main.class": {
                "fileId": "WD6HUHUY"
            }
        }
    }]
}
~~~

**服务器的反馈结果**

~~~bash
[
	{
		"status": "Accepted",
		"exitStatus": 0,
		"time": 96107000,
		"memory": 35115008,
		"runTime": 66561063,
		"files": {
			"stderr": "",
			"stdout": "hello world\n"
		}
	}
]
~~~

## 前端

~~~cmd
npm install
npm run dev
~~~

## 后端启动2个springboot main方法
