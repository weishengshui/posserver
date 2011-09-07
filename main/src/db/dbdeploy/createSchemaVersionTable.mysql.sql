CREATE TABLE IF NOT EXISTS changelog (
  change_number BIGINT NOT NULL PRIMARY KEY,
  complete_dt TIMESTAMP NOT NULL,
  applied_by VARCHAR(100) NOT NULL,
  description VARCHAR(500) NOT NULL
);

-- ALTER TABLE changelog ADD CONSTRAINT Pkchangelog PRIMARY KEY (change_number);