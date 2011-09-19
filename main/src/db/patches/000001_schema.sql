SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

CREATE TABLE Agent (
  id varchar(255) NOT NULL,
  email varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE DeliveryNote (
  id varchar(255) NOT NULL,
  agentName varchar(255) DEFAULT NULL,
  confirmDate datetime DEFAULT NULL,
  createDate datetime DEFAULT NULL,
  dnNumber varchar(255) DEFAULT NULL,
  printDate datetime DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  agent_id varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK326E7A26EE9BB1B9 (agent_id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE DeliveryNoteDetail (
  id varchar(255) NOT NULL,
  model varchar(255) DEFAULT NULL,
  posId varchar(255) DEFAULT NULL,
  simPhoneNo varchar(255) DEFAULT NULL,
  sn varchar(255) DEFAULT NULL,
  dn_id varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK3426CA179B101BF7 (dn_id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE GroupBuyValidateResult (
  id varchar(255) NOT NULL,
  createAt datetime DEFAULT NULL,
  grouponId varchar(255) DEFAULT NULL,
  grouponVCode varchar(255) DEFAULT NULL,
  modifyAt datetime DEFAULT NULL,
  result varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE GrouponCache (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  createDate datetime DEFAULT NULL,
  detailName varchar(255) DEFAULT NULL,
  grouponId varchar(255) DEFAULT NULL,
  grouponName varchar(255) DEFAULT NULL,
  listName varchar(255) DEFAULT NULL,
  mercName varchar(255) DEFAULT NULL,
  posId varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE Journal (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  entity varchar(255) DEFAULT NULL,
  entityId varchar(255) DEFAULT NULL,
  `event` varchar(255) DEFAULT NULL,
  eventDetail longtext,
  ts datetime DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE Pos (
  id varchar(255) NOT NULL,
  firmware VARCHAR(255) DEFAULT NULL,
  dstatus varchar(255) DEFAULT NULL,
  istatus varchar(255) DEFAULT NULL,
  model varchar(255) DEFAULT NULL,
  ostatus varchar(255) DEFAULT NULL,
  posId varchar(255) NOT NULL,
  secret varchar(255) DEFAULT NULL,
  simPhoneNo varchar(255) DEFAULT NULL,
  sn varchar(255) DEFAULT NULL,
  version bigint(20) NOT NULL,
  upgradeRequired BIT,
  PRIMARY KEY (id),
  UNIQUE KEY posId (posId)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE PosAssignment (
  id varchar(255) NOT NULL,
  agent_id varchar(255) DEFAULT NULL,
  pos_id varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK75B26C216BD33719 (pos_id),
  KEY FK75B26C21EE9BB1B9 (agent_id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE ReturnNote (
  id varchar(255) NOT NULL,
  agentName varchar(255) DEFAULT NULL,
  confirmDate datetime DEFAULT NULL,
  createDate datetime DEFAULT NULL,
  printDate datetime DEFAULT NULL,
  rnNumber varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  token varchar(255) DEFAULT NULL,
  agent_id varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FKC6832562EE9BB1B9 (agent_id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE ReturnNoteDetail (
  id varchar(255) NOT NULL,
  dstatus varchar(255) DEFAULT NULL,
  model varchar(255) DEFAULT NULL,
  posId varchar(255) DEFAULT NULL,
  simPhoneNo varchar(255) DEFAULT NULL,
  sn varchar(255) DEFAULT NULL,
  rn_id varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK98C518539B46D481 (rn_id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE ReturnNoteInvitation (
  id varchar(255) NOT NULL,
  requestDate datetime DEFAULT NULL,
  token varchar(255) DEFAULT NULL,
  agent_id varchar(255) DEFAULT NULL,
  PRIMARY KEY (id),
  KEY FK8A36A69BEE9BB1B9 (agent_id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE SysUser (
  username varchar(255) NOT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (username)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE Validation (
  id varchar(255) NOT NULL,
  agentId varchar(255) DEFAULT NULL,
  agentName varchar(255) DEFAULT NULL,
  cstatus varchar(255) DEFAULT NULL,
  currentTime varchar(255) DEFAULT NULL,
  pcode varchar(255) DEFAULT NULL,
  posId varchar(255) DEFAULT NULL,
  posModel varchar(255) DEFAULT NULL,
  posSimPhoneNo varchar(255) DEFAULT NULL,
  refundTime varchar(255) DEFAULT NULL,
  resultExplain varchar(255) DEFAULT NULL,
  resultName varchar(255) DEFAULT NULL,
  resultStatus varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  ts datetime DEFAULT NULL,
  useTime varchar(255) DEFAULT NULL,
  validTime varchar(255) DEFAULT NULL,
  vcode varchar(255) DEFAULT NULL,
  PRIMARY KEY (id)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
