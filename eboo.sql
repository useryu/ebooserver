-- phpMyAdmin SQL Dump
-- version 4.6.2
-- https://www.phpmyadmin.net/
--
-- Host: 10.249.50.199:12745
-- Generation Time: 2017-05-09 07:01:40
-- 服务器版本： 5.6.28-cdb20160902-log
-- PHP Version: 5.6.22

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cAuth`
--
CREATE DATABASE IF NOT EXISTS `cAuth` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `cAuth`;

-- --------------------------------------------------------

--
-- 表的结构 `admin`
--

DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `nickname` varchar(50) NOT NULL,
  `mobile` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `admin`
--

INSERT INTO `admin` (`id`, `username`, `password`, `nickname`, `mobile`) VALUES
(1, 'admin', 'admin', 'admin', '13322985930');

-- --------------------------------------------------------

--
-- 表的结构 `assist`
--

DROP TABLE IF EXISTS `assist`;
CREATE TABLE `assist` (
  `id` int(11) NOT NULL,
  `chapter_id` int(11) NOT NULL,
  `content` text NOT NULL COMMENT '内容'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `assist_result`
--

DROP TABLE IF EXISTS `assist_result`;
CREATE TABLE `assist_result` (
  `id` int(11) NOT NULL,
  `assist_id` int(11) NOT NULL,
  `user_id` varchar(200) NOT NULL,
  `is_done` tinyint(4) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `audio`
--

DROP TABLE IF EXISTS `audio`;
CREATE TABLE `audio` (
  `id` int(11) NOT NULL,
  `chapter_id` int(11) NOT NULL,
  `url` varchar(200) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `audio`
--

INSERT INTO `audio` (`id`, `chapter_id`, `url`) VALUES
(1, 9, 'http://123.207.25.70/resource?filename=a-1-1.mp3'),
(2, 9, 'http://123.207.25.70/resource?filename=a-1-2.mp3');

-- --------------------------------------------------------

--
-- 表的结构 `audio_result`
--

DROP TABLE IF EXISTS `audio_result`;
CREATE TABLE `audio_result` (
  `id` int(11) NOT NULL,
  `audio_id` int(11) NOT NULL,
  `user_id` varchar(200) NOT NULL,
  `is_done` tinyint(4) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `book`
--

DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id` int(11) NOT NULL,
  `name` varchar(200) NOT NULL,
  `level` int(11) NOT NULL COMMENT '级别',
  `no` varchar(20) NOT NULL COMMENT '编号'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `book`
--

INSERT INTO `book` (`id`, `name`, `level`, `no`) VALUES
(1, '新概念1', 0, '1.1.1'),
(2, '新概念2', 0, '0'),
(3, '新概念3', 0, '0'),
(4, '新概念4', 0, '0');

-- --------------------------------------------------------

--
-- 表的结构 `book_result`
--

DROP TABLE IF EXISTS `book_result`;
CREATE TABLE `book_result` (
  `id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `user_id` varchar(200) NOT NULL,
  `audio_is_done` tinyint(4) NOT NULL DEFAULT '0',
  `assist_is_done` tinyint(11) NOT NULL DEFAULT '0',
  `quiz_is_done` tinyint(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `cAppinfo`
--

DROP TABLE IF EXISTS `cAppinfo`;
CREATE TABLE `cAppinfo` (
  `appid` varchar(200) COLLATE utf8_unicode_ci NOT NULL COMMENT 'åº”ç”¨çš„å”¯ä¸€æ ‡è¯†',
  `secret` varchar(300) COLLATE utf8_unicode_ci NOT NULL COMMENT 'åº”ç”¨çš„å¯†é’¥',
  `login_duration` int(11) DEFAULT '30' COMMENT 'é»˜è®¤ç™»é™†æœ‰æ•ˆæœŸï¼Œå•ä½å¤©',
  `session_duration` int(11) DEFAULT '2592000' COMMENT 'é»˜è®¤sessionæœ‰æ•ˆæœŸ,å•ä½ç§’',
  `qcloud_appid` varchar(300) COLLATE utf8_unicode_ci DEFAULT 'appid_qcloud',
  `ip` varchar(50) COLLATE utf8_unicode_ci DEFAULT '0.0.0.0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='å…¨å±€ä¿¡æ¯è¡¨ cAppinfo';

--
-- 转存表中的数据 `cAppinfo`
--

INSERT INTO `cAppinfo` (`appid`, `secret`, `login_duration`, `session_duration`, `qcloud_appid`, `ip`) VALUES
('wxb6d257422d854588', 'e911dc0954afc5cee7b7f47febfff764', 30, 2592000, '1253585104', '10.135.71.251');

-- --------------------------------------------------------

--
-- 表的结构 `chapter`
--

DROP TABLE IF EXISTS `chapter`;
CREATE TABLE `chapter` (
  `id` int(11) NOT NULL,
  `book_id` int(11) NOT NULL,
  `chapter_name` varchar(200) NOT NULL,
  `memo` text NOT NULL,
  `no` int(11) NOT NULL COMMENT '章节号'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `chapter`
--

INSERT INTO `chapter` (`id`, `book_id`, `chapter_name`, `memo`, `no`) VALUES
(9, 1, '第一单元', '', 0),
(10, 1, '第二单元', '', 0),
(11, 1, '第三单元', '', 0),
(12, 1, '第四单元', '', 0),
(13, 1, '第五单元', '', 0),
(14, 1, '第六单元', '', 0);

-- --------------------------------------------------------

--
-- 表的结构 `cSessionInfo`
--

DROP TABLE IF EXISTS `cSessionInfo`;
CREATE TABLE `cSessionInfo` (
  `id` int(11) NOT NULL,
  `uuid` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `skey` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `create_time` datetime NOT NULL,
  `last_visit_time` datetime NOT NULL,
  `open_id` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `session_key` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `user_info` varchar(2048) COLLATE utf8mb4_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会话管理用户信息';

--
-- 转存表中的数据 `cSessionInfo`
--

INSERT INTO `cSessionInfo` (`id`, `uuid`, `skey`, `create_time`, `last_visit_time`, `open_id`, `session_key`, `user_info`) VALUES
(1, 'cc296c139f33a8f8ab7e86cae93da34c', '817724dac5ee75815aa45e39fda28c20', '2017-05-01 11:54:23', '2017-05-01 11:54:23', 'oqkwL0SONfBU1BzyXj7C2IJWJRgw', 'Z6Kxh50lFi0zihrN962ZFw==', 'eyJvcGVuSWQiOiJvcWt3TDBTT05mQlUxQnp5WGo3QzJJSldKUmd3Iiwibmlja05hbWUiOiLpobnnm67nrqHnkIbluIPpgZPogIXkvZnlrpfkvKAiLCJnZW5kZXIiOjEsImxhbmd1YWdlIjoiemhfQ04iLCJjaXR5IjoiU2hlbnpoZW4iLCJwcm92aW5jZSI6Ikd1YW5nZG9uZyIsImNvdW50cnkiOiJDTiIsImF2YXRhclVybCI6Imh0dHA6Ly93eC5xbG9nby5jbi9tbW9wZW4vdmlfMzIvb3F6WG5iOUNrbkpUVGwxODRnNnhBajl3RkJ0ZUxsekE3YjRTZVpIbmljZUs5dVV6SVpWQlB6OEFJcnZBWnhBeTV6OHhyTHE2M1Y4cDlMd2FmbGc2Z2RRLzAiLCJ3YXRlcm1hcmsiOnsidGltZXN0YW1wIjoxNDkzNjEwODYzLCJhcHBpZCI6Ind4MGMyNWM2M2NmY2NlNmEwOSJ9fQ=='),
(2, '7758564407ce119f3d34921e9e31c280', 'dd93ffd6aff5ea4a93d17cfe098e8dc4', '2017-05-01 17:56:26', '2017-05-01 17:56:26', 'oHbT80J1LqzxLVGvyhuXAWBNEgjU', 'TW1VjMiBQ2bdwRaTBuk1NQ==', 'eyJvcGVuSWQiOiJvSGJUODBKMUxxenhMVkd2eWh1WEFXQk5FZ2pVIiwibmlja05hbWUiOiLpobnnm67nrqHnkIbluIPpgZPogIXkvZnlrpfkvKAiLCJnZW5kZXIiOjEsImxhbmd1YWdlIjoiemhfQ04iLCJjaXR5IjoiU2hlbnpoZW4iLCJwcm92aW5jZSI6Ikd1YW5nZG9uZyIsImNvdW50cnkiOiJDTiIsImF2YXRhclVybCI6Imh0dHA6Ly93eC5xbG9nby5jbi9tbW9wZW4vdmlfMzIvN01EQWNtSjFqQVNQZjY2d25xSlNqYlk3MFNMOURYSktJcm1qdmRGa2VGM0xzcXF5Qlgwb3dJNmhWbVZpYnFjTVZUMDVHbmFtOW10aERCbkQwaWFoYnFxdy8wIiwid2F0ZXJtYXJrIjp7InRpbWVzdGFtcCI6MTQ5MzYzMjU4NiwiYXBwaWQiOiJ3eGI2ZDI1NzQyMmQ4NTQ1ODgifX0=');

-- --------------------------------------------------------

--
-- 表的结构 `quiz`
--

DROP TABLE IF EXISTS `quiz`;
CREATE TABLE `quiz` (
  `id` int(11) NOT NULL,
  `no` int(11) NOT NULL COMMENT '编号',
  `chapter_id` int(11) NOT NULL,
  `title` varchar(256) NOT NULL,
  `answer` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `quiz`
--

INSERT INTO `quiz` (`id`, `no`, `chapter_id`, `title`, `answer`) VALUES
(1, 0, 2, 'How are you?', 1),
(2, 0, 2, 'How are you 2?', 1),
(3, 0, 3, 'How are you 3?', 1),
(4, 0, 4, 'How are you 4?', 1),
(5, 0, 4, 'How are you 5?', 1),
(6, 1, 0, 'When can we meet again?', 1),
(7, 2, 0, 'My aunt is going to stay with me.', 5),
(8, 3, 0, 'When do you study?', 8),
(9, 4, 0, 'Would you prefer lemonade or orange juice?', 10),
(10, 5, 0, 'Let\'s have dinner now.', 15),
(11, 6, 0, 'The snow was ...... heavily when I left the house.', 18),
(12, 7, 0, 'I can\'t find my keys anywhere - I ...... have left them at work.', 21),
(13, 8, 0, 'When a car pulled out in front of her, Jane did well not to ...... control of her bicycle.', 25),
(14, 9, 0, 'According to Richard\'s ...... the train leaves at 7 o\'clock.', 31),
(15, 10, 0, 'When you stay in a country for some time you get used to the people\'s ...... of life.', 34),
(16, 11, 0, 'The builders are ...... good progress with the new house.', 38),
(17, 12, 0, 'She is now taking a more positive ...... to her studies and should do well.', 39),
(18, 13, 0, 'My father ...... his new car for two weeks now.', 43),
(19, 14, 0, 'What differences are there ...... the English spoken in the UK and the English spoken in the US?', 48),
(20, 15, 0, 'At 6 p.m. I started to get angry with him because he was late ......', 51),
(21, 16, 0, '...... you get your father\'s permission, I\'ll take you skiing next weekend.', 56),
(22, 17, 0, 'A local company has agreed to ...... the school team with football shirts.', 60),
(23, 18, 0, 'I really enjoy stories that are ...... in the distant future.', 64),
(24, 19, 0, 'That old saucepan will come in ...... when we go camping.', 69),
(25, 20, 0, 'Anyone ...... after the start of the play is not allowed in until the interval.', 73),
(26, 21, 0, 'I didn\'t ...... driving home in the storm so I stayed overnight in a hotel.', 75),
(27, 22, 0, 'The judge said that those prepared to...... in crime must be ready to suffer the consequences.', 80),
(28, 23, 0, 'Marianne seemed to take ...... at my comments on her work.', 85),
(29, 24, 0, 'You should not have a dog if you are not ...... to look after it.', 87),
(30, 25, 0, 'The farmhouse was so isolated that they had to generate their own electricity ......', 92);

-- --------------------------------------------------------

--
-- 表的结构 `quiz_option`
--

DROP TABLE IF EXISTS `quiz_option`;
CREATE TABLE `quiz_option` (
  `id` int(11) NOT NULL,
  `quiz_id` int(11) NOT NULL,
  `text` varchar(256) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `quiz_option`
--

INSERT INTO `quiz_option` (`id`, `quiz_id`, `text`) VALUES
(1, 6, 'When are you free?'),
(2, 6, 'It was two days ago.'),
(3, 6, 'Can you help me?'),
(4, 7, 'How do you do?'),
(5, 7, 'How long for?'),
(6, 7, 'How was it?'),
(7, 8, 'at school'),
(8, 8, 'in the evenings'),
(9, 8, 'in the library'),
(10, 9, 'Have you got anything else?'),
(11, 9, 'If you like.'),
(12, 9, 'Are you sure about that?'),
(13, 10, 'You aren\'t eating.'),
(14, 10, 'There aren\'t any.'),
(15, 10, 'Tom isn\'t here yet'),
(16, 11, 'dropping'),
(17, 11, 'landing'),
(18, 11, 'falling'),
(19, 11, 'descending'),
(20, 12, 'can'),
(21, 12, 'must'),
(22, 12, 'ought'),
(23, 12, 'would'),
(24, 13, 'miss'),
(25, 13, 'lose'),
(26, 13, 'fail'),
(27, 13, 'drop'),
(28, 14, 'opinion'),
(29, 14, 'advice'),
(30, 14, 'knowledge'),
(31, 14, 'information'),
(32, 15, 'habit'),
(33, 15, 'custom'),
(34, 15, 'way'),
(35, 15, 'system'),
(36, 16, 'getting'),
(37, 16, 'doing'),
(38, 16, 'making'),
(39, 17, 'attitude'),
(40, 17, 'behaviour'),
(41, 17, 'manner'),
(42, 17, 'style'),
(43, 18, 'has had'),
(44, 18, 'has'),
(45, 18, 'is having'),
(46, 18, 'had'),
(47, 19, 'among'),
(48, 19, 'between'),
(49, 19, 'beside'),
(50, 19, 'with'),
(51, 20, 'as usual.'),
(52, 20, 'in general.'),
(53, 20, 'typically.'),
(54, 20, 'usually.'),
(55, 21, 'Although'),
(56, 21, 'Provided'),
(57, 21, 'As'),
(58, 21, 'Unless'),
(59, 22, 'contribute'),
(60, 22, 'supply'),
(61, 22, 'give'),
(62, 22, 'produce'),
(63, 23, 'found'),
(64, 23, 'set'),
(65, 23, 'put'),
(66, 23, 'placed'),
(67, 24, 'convenient'),
(68, 24, 'fitting'),
(69, 24, 'handy'),
(70, 24, 'suitable'),
(71, 25, 'arrives'),
(72, 25, 'has arrived'),
(73, 25, 'arriving'),
(74, 25, 'arrived'),
(75, 26, 'fancy'),
(76, 26, 'desire'),
(77, 26, 'prefer'),
(78, 26, 'want'),
(79, 27, 'involve'),
(80, 27, 'engage'),
(81, 27, 'undertake'),
(82, 27, 'enlist'),
(83, 28, 'annoyance'),
(84, 28, 'insult'),
(85, 28, 'offence'),
(86, 28, 'indignation'),
(87, 29, 'prepared'),
(88, 29, 'adapted'),
(89, 29, 'arranged'),
(90, 29, 'decided'),
(91, 30, 'current.'),
(92, 30, 'supply.'),
(93, 30, 'grid.'),
(94, 30, 'power.');

-- --------------------------------------------------------

--
-- 表的结构 `quiz_result`
--

DROP TABLE IF EXISTS `quiz_result`;
CREATE TABLE `quiz_result` (
  `id` int(11) NOT NULL,
  `quiz_id` int(11) NOT NULL,
  `user_id` varchar(200) NOT NULL,
  `is_right` tinyint(4) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(100) NOT NULL COMMENT '用户ID',
  `point` int(11) NOT NULL DEFAULT '-1' COMMENT '当前分数',
  `pre_point` int(11) NOT NULL DEFAULT '-1' COMMENT '上一次分数',
  `is_member` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否会员',
  `member_start_dt` datetime DEFAULT NULL COMMENT '开始日期',
  `member_end_dt` datetime DEFAULT NULL COMMENT '结束日期',
  `level` int(11) NOT NULL DEFAULT '0' COMMENT '级别，测试后得到',
  `reading_book_id` int(11) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `user`
--

INSERT INTO `user` (`id`, `point`, `pre_point`, `is_member`, `member_start_dt`, `member_end_dt`, `level`, `reading_book_id`) VALUES
('7758564407ce119f3d34921e9e31c280', -1, -1, 0, NULL, NULL, 0, 1),
('cc296c139f33a8f8ab7e86cae93da34c', -1, -1, 0, NULL, NULL, 0, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `assist`
--
ALTER TABLE `assist`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `assist_result`
--
ALTER TABLE `assist_result`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `audio`
--
ALTER TABLE `audio`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `audio_result`
--
ALTER TABLE `audio_result`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `book_result`
--
ALTER TABLE `book_result`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `cAppinfo`
--
ALTER TABLE `cAppinfo`
  ADD PRIMARY KEY (`appid`);

--
-- Indexes for table `chapter`
--
ALTER TABLE `chapter`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `cSessionInfo`
--
ALTER TABLE `cSessionInfo`
  ADD PRIMARY KEY (`id`),
  ADD KEY `auth` (`uuid`,`skey`),
  ADD KEY `weixin` (`open_id`,`session_key`);

--
-- Indexes for table `quiz`
--
ALTER TABLE `quiz`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `quiz_option`
--
ALTER TABLE `quiz_option`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `quiz_result`
--
ALTER TABLE `quiz_result`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`);

--
-- 在导出的表使用AUTO_INCREMENT
--

--
-- 使用表AUTO_INCREMENT `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- 使用表AUTO_INCREMENT `assist`
--
ALTER TABLE `assist`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- 使用表AUTO_INCREMENT `assist_result`
--
ALTER TABLE `assist_result`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- 使用表AUTO_INCREMENT `audio`
--
ALTER TABLE `audio`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- 使用表AUTO_INCREMENT `audio_result`
--
ALTER TABLE `audio_result`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- 使用表AUTO_INCREMENT `book`
--
ALTER TABLE `book`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- 使用表AUTO_INCREMENT `book_result`
--
ALTER TABLE `book_result`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
--
-- 使用表AUTO_INCREMENT `chapter`
--
ALTER TABLE `chapter`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;
--
-- 使用表AUTO_INCREMENT `cSessionInfo`
--
ALTER TABLE `cSessionInfo`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- 使用表AUTO_INCREMENT `quiz`
--
ALTER TABLE `quiz`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=56;
--
-- 使用表AUTO_INCREMENT `quiz_option`
--
ALTER TABLE `quiz_option`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=189;
--
-- 使用表AUTO_INCREMENT `quiz_result`
--
ALTER TABLE `quiz_result`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
