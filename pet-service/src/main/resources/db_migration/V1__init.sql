CREATE TABLE IF NOT EXISTS pets (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    birth_date DATE NOT NULL,
    breed VARCHAR(255) NOT NULL,
    color VARCHAR(50) NOT NULL,
    owner_id BIGINT
);

CREATE TABLE IF NOT EXISTS pet_friends (
    pet_id BIGINT REFERENCES pets(id),
    friend_id BIGINT REFERENCES pets(id),
    PRIMARY KEY (pet_id, friend_id)
);