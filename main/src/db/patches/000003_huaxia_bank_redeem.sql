CREATE TABLE `huaxiaredeem` (
   `id` varchar(255) NOT NULL,
   `agentId` varchar(255) DEFAULT NULL,
   `agentName` varchar(255) DEFAULT NULL,
   `posId` varchar(255) DEFAULT NULL,
   `posModel` varchar(255) DEFAULT NULL,
   `posSimPhoneNo` varchar(255) DEFAULT NULL,
   `serialNum` varchar(255) DEFAULT NULL,
   `cardNum` varchar(255) DEFAULT NULL,
   `orderDate` varchar(255) DEFAULT NULL,
   `status` varchar(255) DEFAULT NULL,
   `createDate` datetime DEFAULT NULL,
   `confirmDate` datetime DEFAULT NULL,
   `ackDate` datetime DEFAULT NULL,
   `ackId` varchar(255) DEFAULT NULL,
   `batchNum` varchar(255) DEFAULT NULL,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;