
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `content` mediumtext,
  `time` datetime DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `type` bigint(20) DEFAULT NULL,
  `views` bigint(20) DEFAULT NULL,
  `keywords` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=118 DEFAULT CHARSET=utf8;

 
DROP TABLE IF EXISTS `channel`;
CREATE TABLE `channel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `rows` int(11) DEFAULT NULL,
  `name` varchar(20) DEFAULT NULL,
  `template` varchar(255) DEFAULT NULL,
  `hide` tinyint(1) DEFAULT NULL,
  `keywords` varchar(100) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `module` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=73 DEFAULT CHARSET=gbk;
 
INSERT INTO `channel` VALUES ('1', '0', '1', '10', '首 页', 'index.html', '1', '雨林建站系统新闻中心', '雨林内容管理系统是有marker结合各大内容管理系统开发的一套解决企业建站方案的产品', 'index', '12', 'channel');
INSERT INTO `channel` VALUES ('2', '0', '3', '0', '演示栏目', 'template.html', '1', '演示栏目', '栏目类型的演示栏目', 'demo', '12', 'channel');
INSERT INTO `channel` VALUES ('72', '0', '0', '10', '成功案例', 'anli.html', '0', '成功案例', '成功案例dsa ', 'defalut', '', 'article');
INSERT INTO `channel` VALUES ('13', '0', '11', '10', '文章栏目', 'help.html', '1', '文章栏目', '文章栏目是可以发布文章的栏目', 'articlechannel', '', 'article');
INSERT INTO `channel` VALUES ('15', '0', '11', '10', '在线留言', 'guestbook.html', '1', '雨林建站系统新闻中心', 'dsadsad', 'guestbook', '12', 'channel');
 
DROP TABLE IF EXISTS `chip`;
CREATE TABLE `chip` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `content` text,
  `mark` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `status` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=120 DEFAULT CHARSET=utf8;

 
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `power` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=78 DEFAULT CHARSET=utf8;
 
INSERT INTO `group` VALUES ('1', '超级管理员', '1,2,3,4,5,6');
 
DROP TABLE IF EXISTS `guestbook`;
CREATE TABLE `guestbook` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `content` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;
 
INSERT INTO `guestbook` VALUES ('19', '倒萨倒萨打算倒萨打算', '127.0.0.1', '2013-01-20 22:54:19', '吴伟');
INSERT INTO `guestbook` VALUES ('27', '我aid撒的撒', '127.0.0.1', '2013-01-27 21:54:10', '吴伟');
INSERT INTO `guestbook` VALUES ('28', 'dsa fdsf dsaf dsfdsafdsfdsafsdafdsafdsafdsafdsfdsafdsa', '127.0.0.1', '2013-01-27 21:58:46', '吴伟');
INSERT INTO `guestbook` VALUES ('23', '倒萨倒萨倒萨倒萨', '127.0.0.1', '2013-01-20 23:52:30', '吴伟');
INSERT INTO `guestbook` VALUES ('26', '242142314231432143214321421423143', '127.0.0.1', '2013-01-27 21:32:25', '吴伟');
 
DROP TABLE IF EXISTS `links`;
CREATE TABLE `links` (
  `yl_id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`yl_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
 
DROP TABLE IF EXISTS `module`;
CREATE TABLE `module` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `template` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
 
INSERT INTO `module` VALUES ('2', '文章模型', 'org.marker.mushroom.module.impl.ArticleModule', 'content', 'article.html', 'article');
INSERT INTO `module` VALUES ('1', '栏目模型', 'org.marker.mushroom.module.impl.ChannelModule', '', '', 'channel');
 
DROP TABLE IF EXISTS `plugin`;
CREATE TABLE `plugin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `uri` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `mark` varchar(255) DEFAULT NULL,
  `author` varchar(20) DEFAULT NULL,
  `ver` varchar(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
 
INSERT INTO `plugin` VALUES ('8', '留言插件', 'org.marker.mushroom.plugins.impl.CommentPluginImpl', '1', 'comment', 'marker', '1.0');
INSERT INTO `plugin` VALUES ('9', '搜索插件', 'org.marker.mushroom.plugins.impl.WebSearchPluginImpl', '1', 'search', 'marker', '1.0');
 
DROP TABLE IF EXISTS `searchinfo`;
CREATE TABLE `searchinfo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `keyword` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=121 DEFAULT CHARSET=utf8;
 
INSERT INTO `searchinfo` VALUES ('118', 'a', '0:0:0:0:0:0:0:1', '2013-03-12 10:27:40');
INSERT INTO `searchinfo` VALUES ('119', 'a', '0:0:0:0:0:0:0:1', '2013-03-12 10:27:43');
INSERT INTO `searchinfo` VALUES ('120', 'æ', '0:0:0:0:0:0:0:1', '2013-03-12 10:28:32');
 
DROP TABLE IF EXISTS `template`;
CREATE TABLE `template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `time` datetime DEFAULT NULL,
  `durl` varchar(255) DEFAULT NULL,
  `dloaded` bigint(20) DEFAULT NULL,
  `grade` int(11) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `icon` varchar(255) DEFAULT NULL,
  `description` mediumtext,
  `views` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
 
INSERT INTO `template` VALUES ('1', '的撒到底是啊', '2013-04-23 23:48:23', 'http://dsadsa.dsadsadsa,com/dsadsa', '0', '0', '40', '跌名', 'upload/images/shuangzhi.png', '的撒的撒', '312');
INSERT INTO `template` VALUES ('2', 'dsadsad', '2013-04-11 00:09:24', 'http://dsadsa.dsadsadsa,com/dsadsa', '0', '0', '40', 'sad', 'upload/images/shuangzhi.png', '的撒的撒', '197');
INSERT INTO `template` VALUES ('3', 'sadsadsad', '2013-04-24 00:20:03', 'http://dsadsa.dsadsadsa,com/dsadsa', '0', '0', '40', '跌名sad', 'upload/images/shuangzhi.png', 'sad', '21');
 
DROP TABLE IF EXISTS `tmp_for_channel`;
CREATE TABLE `tmp_for_channel` (
  `id` bigint(20) DEFAULT NULL,
  `pid` bigint(20) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
 
INSERT INTO `tmp_for_channel` VALUES ('2', '0');
INSERT INTO `tmp_for_channel` VALUES ('40', '2');
INSERT INTO `tmp_for_channel` VALUES ('41', '2');
INSERT INTO `tmp_for_channel` VALUES ('42', '2');
INSERT INTO `tmp_for_channel` VALUES ('43', '2');
INSERT INTO `tmp_for_channel` VALUES ('44', '2');
INSERT INTO `tmp_for_channel` VALUES ('45', '2');
 
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `pass` varchar(255) NOT NULL,
  `group` int(11) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `createtime` datetime DEFAULT NULL,
  `logintime` datetime DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=129 DEFAULT CHARSET=gbk;
 
INSERT INTO `user` VALUES ('1', 'admin', '123', '1', '雨林博客', '2013-02-01 20:57:00', '2013-01-31 20:59:49', '1', '网站超级管理员');
