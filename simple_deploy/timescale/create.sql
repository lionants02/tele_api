CREATE TABLE "activity" (
	"time" TIMESTAMP NOT NULL DEFAULT 'now()',
	"type" VARCHAR(10) NOT NULL,
	"message" TEXT NOT NULL
);

CREATE TABLE "activity_queue" (
	"time" TIMESTAMP NOT NULL DEFAULT 'now()',
	"queue_code" VARCHAR(50) NOT NULL,
	"activity" VARCHAR(10) NOT NULL,
	"message" TEXT NOT NULL
);

SELECT create_hypertable('activity','time');
SELECT create_hypertable('activity_queue','time');