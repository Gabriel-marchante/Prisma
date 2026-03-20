use mediaServer

CREATE TABLE video (
    id BIGINT PRIMARY KEY,
    titol VARCHAR(50),
    duracio FLOAT,
    codec VARCHAR(10),
    resolucio VARCHAR(10),
    pes BIGINT
);