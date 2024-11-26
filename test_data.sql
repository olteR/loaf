INSERT INTO users (id, email, name, password)
VALUES (1, 'alfonz31@email.com', 'alfonz31', '$2a$10$yrIBgrnKfTloOEJx.VG22eghjdQJxmU/1jkosrWZZ1vswCwd/OurS'),
       (2, 'bartal50@email.com', 'bartal50', '$2a$10$yrIBgrnKfTloOEJx.VG22eghjdQJxmU/1jkosrWZZ1vswCwd/OurS'),
       (3, 'cirilla77@email.com', 'cirilla77', '$2a$10$yrIBgrnKfTloOEJx.VG22eghjdQJxmU/1jkosrWZZ1vswCwd/OurS'),
       (4, 'demeter47@email.com', 'demeter47', '$2a$10$yrIBgrnKfTloOEJx.VG22eghjdQJxmU/1jkosrWZZ1vswCwd/OurS'),
       (5, 'elmira92@email.com', 'elmira92', '$2a$10$yrIBgrnKfTloOEJx.VG22eghjdQJxmU/1jkosrWZZ1vswCwd/OurS'),
       (6, 'frigyes19@email.com', 'frigyes19', '$2a$10$yrIBgrnKfTloOEJx.VG22eghjdQJxmU/1jkosrWZZ1vswCwd/OurS'),
       (7, 'gilda27@email.com', 'gilda27', '$2a$10$yrIBgrnKfTloOEJx.VG22eghjdQJxmU/1jkosrWZZ1vswCwd/OurS'),
       (8, 'hont9@email.com', 'hont9', '$2a$10$yrIBgrnKfTloOEJx.VG22eghjdQJxmU/1jkosrWZZ1vswCwd/OurS'),
       (9, 'ingrid7@email.com', 'ingrid7', '$2a$10$yrIBgrnKfTloOEJx.VG22eghjdQJxmU/1jkosrWZZ1vswCwd/OurS'),
       (10, 'jakab68@email.com', 'jakab68', '$2a$10$yrIBgrnKfTloOEJx.VG22eghjdQJxmU/1jkosrWZZ1vswCwd/OurS'),
       (11, 'olter@email.com', 'olter', '$2a$10$yrIBgrnKfTloOEJx.VG22eghjdQJxmU/1jkosrWZZ1vswCwd/OurS');

INSERT INTO games (id, turn, downward_discard, killed_character, robbed_character, bewitched_character, is_final_turn, phase, current_player)
VALUES (1, 0,null, null, null, null, false, 'NOT_STARTED', null),
       (2, 0, null, null, null, null, false, 'NOT_STARTED', null);

INSERT INTO lobbies (id, code, max_members, name, owner, password, secured, status, game_id)
VALUES (1, 'N0X5T4R7', 7, 'alfonz31 lobbija', 1, null, false, 'CREATED', 1),
       (2, 'PR07XL08', 7, 'cirilla77 csapata', 3, '$2a$10$yrIBgrnKfTloOEJx.VG22eghjdQJxmU/1jkosrWZZ1vswCwd/OurS', true, 'CREATED', 2);

INSERT INTO lobby_users (lobby_id, user_id)
VALUES (1, 1), (1, 2),(1, 3), (1, 4),
       (2, 3);

INSERT INTO players (id, user_id, gold, points, build_limit, revealed, ability_target, character_number, player_order, using_ability, game_id)
VALUES (1, 1, null, null, null, false, null, null, 1, null, 1),
       (2, 2, null, null, null, false, null, null, 2, null, 1),
       (3, 3, null, null, null, false, null, null, 3, null, 1),
       (4, 4, null, null, null, false, null, null, 4, null, 1),
       (5, 3, null, null, null, false, null, null, 1, null, 2);

INSERT INTO game_characters (game_id, character_id)
VALUES (1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8),
       (2, 1), (2, 2), (2, 3), (2, 4), (2, 5), (2, 6), (2, 7), (2, 8);

INSERT INTO game_unique_districts (game_id, district_id)
VALUES (1, 39), (1, 28), (1, 30), (1, 21), (1, 26), (1, 35), (1, 33), (1, 44), (1, 34), (1, 47), (1, 32), (1, 42), (1, 46), (1, 31),
       (2, 39), (2, 28), (2, 30), (2, 21), (2, 26), (2, 35), (2, 33), (2, 44), (2, 34), (2, 47), (2, 32), (2, 42), (2, 46), (2, 31);