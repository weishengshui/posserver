CREATE TABLE QQMeishiXaction (
	id VARCHAR(255) NOT NULL,
	agentId VARCHAR(255),
	agentName VARCHAR(255),
	consumeAmount double precision NOT NULL,
	forcePwdOnNextAction bit NOT NULL,
	posId VARCHAR(255),
	posModel VARCHAR(255),
	posSimPhoneNo VARCHAR(255),
	qqUserToken VARCHAR(255),
	receiptTip VARCHAR(255),
	receiptTitle VARCHAR(255),
	remoteXactDate datetime,
	remoteXactPwd VARCHAR(255),
	ts datetime,
	xactPwd VARCHAR(255),
	xactResultCode integer NOT NULL,
	primary key (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;