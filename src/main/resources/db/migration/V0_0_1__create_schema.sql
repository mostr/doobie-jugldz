CREATE TABLE users (
  id            UUID PRIMARY KEY,
  email         VARCHAR NOT NULL,
  password_hash VARCHAR NOT NULL,
  referral_code VARCHAR DEFAULT NULL,
  date_of_birth DATE    NOT NULL,
  UNIQUE (email)
);

-- insert some data
INSERT INTO users
VALUES ('2f09f05a-4869-4a31-8f99-b83f298c2c94', 'alice@example.com', 'pwdhash', NULL, '1990-05-01');
INSERT INTO users
VALUES ('b6d6b4cd-da9f-4341-87b9-0e1224febb93', 'bob@example.com', 'pwdhash', 'promo123', '2000-11-22');

CREATE TABLE verifications (
  id                UUID PRIMARY KEY,
  user_id           UUID        NOT NULL REFERENCES users (id),
  status            VARCHAR     NOT NULL,
  verification_type VARCHAR     NOT NULL,
  created_at        TIMESTAMPTZ NOT NULL
);