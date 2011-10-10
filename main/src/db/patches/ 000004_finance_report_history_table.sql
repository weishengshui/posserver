CREATE TABLE `financereporthistory` (
  `id` varchar(255) NOT NULL,
  `agentId` varchar(255) DEFAULT NULL,
  `agentName` varchar(255) DEFAULT NULL,
  `createDate` datetime DEFAULT NULL,
  `endDate` datetime DEFAULT NULL,
  `modifyDate` datetime DEFAULT NULL,
  `reportDetail` longtext,
  `startDate` datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8
