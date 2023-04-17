            create table kingdom_meta (
            id int4 NOT null primary key,
            full_name varchar not null,
            rgb_color int4 not null
            );

            insert into kingdom_meta (
            id, full_name, rgb_color
            ) values
            (COALESCE((SELECT MAX(id) FROM kingdom_meta), 0) + 1, 'RED', 0xf80000),
            (COALESCE((SELECT MAX(id) FROM kingdom_meta), 0) + 2, 'BLUE', 0x3050f8),
            (COALESCE((SELECT MAX(id) FROM kingdom_meta), 0) + 3, 'TAN', 0x987450),
            (COALESCE((SELECT MAX(id) FROM kingdom_meta), 0) + 4, 'GREEN', 0x409428),
            (COALESCE((SELECT MAX(id) FROM kingdom_meta), 0) + 5, 'ORANGE', 0xf88000),
            (COALESCE((SELECT MAX(id) FROM kingdom_meta), 0) + 6, 'PURPLE', 0x882ca0),
            (COALESCE((SELECT MAX(id) FROM kingdom_meta), 0) + 7, 'TEAL', 0x0898a0),
            (COALESCE((SELECT MAX(id) FROM kingdom_meta), 0) + 8, 'PINK', 0xc07888);

            create table town_build_anim (
            id int4 not null primary key,
            full_name varchar not null,
            hall_picture_id int4 not null,
            hall_picture_row int4 not null,
            hall_picture_col int4 not null,
            picture_width int4 not null,
            picture_height int4 not null,
            picture_count int4 not null,
            picture_left int4 not null,
            picture_top int4 not null,
            picture_layer int4 not null
            );

            create table town_building (
            id int4 not null primary key,
            full_name varchar not null,
            castle_id int4 not null,
            parent_id int4,
            animation_id int4 not null,
            wood int4 not null,
            mercury int4 not null,
            ore int4 not null,
            sulfur int4 not null,
            crystal int4 not null,
            gems int4 not null,
            gold int4 not null,
            CONSTRAINT fk_castle FOREIGN KEY(castle_id) REFERENCES castle(id),
            CONSTRAINT fk_animation FOREIGN KEY(animation_id) REFERENCES town_build_anim(id),
            CONSTRAINT fk_parent FOREIGN KEY(parent_id) REFERENCES town_building(id)
            );

            create table building_price (
            id int4 not null primary key,
            building_id int4 not null,
            resource_id int4 not null,
            res_count int4 not null,
            CONSTRAINT fk_building FOREIGN KEY(building_id) REFERENCES town_building(id),
            CONSTRAINT fk_resource FOREIGN KEY(resource_id) REFERENCES resource(id)
            );

            create table building_prereq (
            id int4 not null primary key,
            building_id int4 not null,
            prereq_id int4 not null,
            CONSTRAINT fk_building FOREIGN KEY(building_id) REFERENCES town_building(id),
            CONSTRAINT fk_prereq FOREIGN KEY(prereq_id) REFERENCES town_building(id)
            );

            create table additional_build_anim (
            id int4 not null primary key,
            building_id int4 not null,
            animation_id int4 not null,
            existing_building_id int4,
            CONSTRAINT fk_building FOREIGN KEY(building_id) REFERENCES town_building(id),
            CONSTRAINT fk_animation FOREIGN KEY(animation_id) REFERENCES town_build_anim(id),
            CONSTRAINT fk_existing FOREIGN KEY(existing_building_id) REFERENCES town_building(id)
            );

            -- CASTLE
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            (COALESCE((SELECT MAX(id) FROM town_build_anim), 0) + 1,'CASTLE_VILLAGE_HALL',10,0,0,216,129,10,0,218,4),
            (COALESCE((SELECT MAX(id) FROM town_build_anim), 0) + 2,'COLOSSUS',26,0,0,60,89,1,456,109,0),
            (COALESCE((SELECT MAX(id) FROM town_build_anim), 0) + 3,'CASTLE_FORT',7,0,1,205,124,1,595,66,1),
            (COALESCE((SELECT MAX(id) FROM town_build_anim), 0) + 4,'CASTLE_TAVERN',5,0,2,105,144,1,0,230,5),
            (COALESCE((SELECT MAX(id) FROM town_build_anim), 0) + 5,'CASTLE_BLACKSMITH',16,0,3,153,123,1,213,251,5),
            (COALESCE((SELECT MAX(id) FROM town_build_anim), 0) + 6,'CASTLE_MARKETPLACE',14,1,0,291,110,1,413,264,5),
            (COALESCE((SELECT MAX(id) FROM town_build_anim), 0) + 7,'CASTLE_MAGE_GUILD_1',0,1,1,93,103,1,707,166,2),
            (COALESCE((SELECT MAX(id) FROM town_build_anim), 0) + 8,'CASTLE_SHIPYARD',6,1,2,140,104,1,478,134,2),
            (COALESCE((SELECT MAX(id) FROM town_build_anim), 0) + 9,'CASTLE_CITADEL_SHIPYARD',0,0,0,140,104,1,478,134,2),
            (COALESCE((SELECT MAX(id) FROM town_build_anim), 0) + 10,'CASTLE_WATERFALL',0,0,0,24,38,10,46,119,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            (COALESCE((SELECT MAX(id) FROM town_building), 0) + 1,'CASTLE_VILLAGE_HALL',(SELECT id FROM castle WHERE full_name='CASTLE'),null,(SELECT id from town_build_anim where full_name = 'CASTLE_VILLAGE_HALL'),0,0,0,0,0,0,0),
            (COALESCE((SELECT MAX(id) FROM town_building), 0) + 2,'COLOSSUS',(SELECT id FROM castle WHERE full_name='CASTLE'),null,(SELECT id from town_build_anim where full_name = 'COLOSSUS'),0,0,0,0,0,0,0),
            (COALESCE((SELECT MAX(id) FROM town_building), 0) + 3,'CASTLE_FORT',(SELECT id FROM castle WHERE full_name='CASTLE'),null,(SELECT id from town_build_anim where full_name = 'CASTLE_FORT'),20,0,20,0,0,0,5000),
            (COALESCE((SELECT MAX(id) FROM town_building), 0) + 4,'CASTLE_TAVERN',(SELECT id FROM castle WHERE full_name='CASTLE'),null,(SELECT id from town_build_anim where full_name = 'CASTLE_TAVERN'),5,0,0,0,0,0,500),
            (COALESCE((SELECT MAX(id) FROM town_building), 0) + 5,'CASTLE_BLACKSMITH',(SELECT id FROM castle WHERE full_name='CASTLE'),null,(SELECT id from town_build_anim where full_name = 'CASTLE_BLACKSMITH'),5,0,0,0,0,0,500),
            (COALESCE((SELECT MAX(id) FROM town_building), 0) + 6,'CASTLE_MARKETPLACE',(SELECT id FROM castle WHERE full_name='CASTLE'),null,(SELECT id from town_build_anim where full_name = 'CASTLE_MARKETPLACE'),5,0,0,0,0,0,500),
            (COALESCE((SELECT MAX(id) FROM town_building), 0) + 7,'CASTLE_MAGE_GUILD_1',(SELECT id FROM castle WHERE full_name='CASTLE'),null,(SELECT id from town_build_anim where full_name = 'CASTLE_MAGE_GUILD_1'),5,0,5,0,0,0,2000),
            (COALESCE((SELECT MAX(id) FROM town_building), 0) + 8,'CASTLE_SHIPYARD',(SELECT id FROM castle WHERE full_name='CASTLE'),null,(SELECT id from town_build_anim where full_name = 'CASTLE_SHIPYARD'),20,0,0,0,0,0,2000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'CASTLE_TOWN_HALL',11,0,0,216,171,10,0,176,4),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'CASTLE_RESOURCE_SILO',15,1,0,42,75,1,488,228,4),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'CASTLE_CITADEL',8,0,1,322,181,1,478,66,1),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'CASTLE_MAGE_GUILD_2',1,1,1,93,134,1,707,135,2);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'CASTLE_TOWN_HALL',(SELECT id FROM castle WHERE full_name = 'CASTLE'),(SELECT id FROM town_building WHERE full_name='CASTLE_VILLAGE_HALL'),(SELECT id from town_build_anim where full_name = 'CASTLE_TOWN_HALL'),0,0,0,0,0,0,2500),
            ((SELECT MAX(id) + 2 FROM town_building),'CASTLE_RESOURCE_SILO',(SELECT id FROM castle WHERE full_name = 'CASTLE'),null,(SELECT id from town_build_anim where full_name = 'CASTLE_RESOURCE_SILO'),0,0,5,0,0,0,5000),
            ((SELECT MAX(id) + 3 FROM town_building),'CASTLE_CITADEL',(SELECT id FROM castle WHERE full_name = 'CASTLE'),(SELECT id FROM town_building WHERE full_name='CASTLE_FORT'),(SELECT id from town_build_anim where full_name = 'CASTLE_CITADEL'),0,0,5,0,0,0,2500),
            ((SELECT MAX(id) + 4 FROM town_building),'CASTLE_MAGE_GUILD_2',(SELECT id FROM castle WHERE full_name = 'CASTLE'),(SELECT id FROM town_building WHERE full_name='CASTLE_MAGE_GUILD_1'),(SELECT id from town_build_anim where full_name = 'CASTLE_MAGE_GUILD_2'),5,4,5,4,4,4,5000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'CASTLE_CITY_HALL',12,0,0,216,183,10,0,164,4),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'CASTLE_CASTLE',9,0,1,322,210,1,478,37,1),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'CASTLE_MAGE_GUILD_3',2,1,1,96,162,11,704,107,2);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'CASTLE_CITY_HALL',(SELECT id FROM castle WHERE full_name = 'CASTLE'),(SELECT id FROM town_building WHERE full_name='CASTLE_TOWN_HALL'),(SELECT id from town_build_anim where full_name = 'CASTLE_CITY_HALL'),0,0,0,0,0,0,5000),
            ((SELECT MAX(id) + 2 FROM town_building),'CASTLE_CASTLE',(SELECT id FROM castle WHERE full_name = 'CASTLE'),(SELECT id FROM town_building WHERE full_name='CASTLE_CITADEL'),(SELECT id from town_build_anim where full_name = 'CASTLE_CASTLE'),10,0,10,0,0,0,5000),
            ((SELECT MAX(id) + 3 FROM town_building),'CASTLE_MAGE_GUILD_3',(SELECT id FROM castle WHERE full_name = 'CASTLE'),(SELECT id FROM town_building WHERE full_name='CASTLE_MAGE_GUILD_2'),(SELECT id from town_build_anim where full_name = 'CASTLE_MAGE_GUILD_3'),5,6,5,6,6,6,1000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'CASTLE_CAPITOL',13,0,0,216,193,10,0,154,4),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'CASTLE_MAGE_GUILD_4',3,1,1,96,193,11,704,76,2);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'CASTLE_CAPITOL',(SELECT id FROM castle WHERE full_name = 'CASTLE'),(SELECT id FROM town_building WHERE full_name='CASTLE_CITY_HALL'),(SELECT id from town_build_anim where full_name = 'CASTLE_CAPITOL'),0,0,0,0,0,0,10000),
            ((SELECT MAX(id) + 2 FROM town_building),'CASTLE_MAGE_GUILD_4',(SELECT id FROM castle WHERE full_name = 'CASTLE'),(SELECT id FROM town_building WHERE full_name='CASTLE_MAGE_GUILD_3'),(SELECT id from town_build_anim where full_name = 'CASTLE_MAGE_GUILD_4'),5,8,5,8,8,8,1000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'GUARDHOUSE',30,3,0,61,85,1,304,92,1),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'ARCHERS_TOWER',31,3,1,113,56,1,360,130,1),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'GRIFFIN_TOWER',32,3,2,100,121,1,76,57,1),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'BARRACKS',33,3,3,143,64,1,176,101,1),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'MONASTERY',34,4,0,138,49,1,563,211,3),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'TRAINING_GROUNDS',35,4,1,287,108,10,174,190,3),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'PORTAL_OF_GLORY',36,4,2,286,116,1,303,0,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'GUARDHOUSE',(SELECT id FROM castle WHERE full_name='CASTLE'),null,(SELECT id from town_build_anim where full_name = 'GUARDHOUSE'),0,0,10,0,0,0,500),
            ((SELECT MAX(id) + 2 FROM town_building),'ARCHERS_TOWER',(SELECT id FROM castle WHERE full_name='CASTLE'),null,(SELECT id from town_build_anim where full_name = 'ARCHERS_TOWER'),5,0,5,0,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'GRIFFIN_TOWER',(SELECT id FROM castle WHERE full_name='CASTLE'),null,(SELECT id from town_build_anim where full_name = 'GRIFFIN_TOWER'),0,0,5,0,0,0,1000),
            ((SELECT MAX(id) + 4 FROM town_building),'BARRACKS',(SELECT id FROM castle WHERE full_name='CASTLE'),null,(SELECT id from town_build_anim where full_name = 'BARRACKS'),0,0,5,0,0,0,2000),
            ((SELECT MAX(id) + 5 FROM town_building),'MONASTERY',(SELECT id FROM castle WHERE full_name='CASTLE'),null,(SELECT id from town_build_anim where full_name = 'MONASTERY'),5,2,5,2,2,2,3000),
            ((SELECT MAX(id) + 6 FROM town_building),'TRAINING_GROUNDS',(SELECT id FROM castle WHERE full_name='CASTLE'),null,(SELECT id from town_build_anim where full_name = 'TRAINING_GROUNDS'),20,0,0,0,0,0,5000),
            ((SELECT MAX(id) + 7 FROM town_building),'PORTAL_OF_GLORY',(SELECT id FROM castle WHERE full_name='CASTLE'),null,(SELECT id from town_build_anim where full_name = 'PORTAL_OF_GLORY'),0,10,0,10,10,10,20000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'UPGR_GUARDHOUSE',37,3,0,64,111,1,304,65,1),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'UPGR_ARCHERS_TOWER',38,3,1,113,71,1,360,115,1),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'UPGR_GRIFFIN_TOWER',39,3,2,100,143,1,76,35,1),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'UPGR_BARRACKS',40,3,3,143,80,1,176,85,1),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'UPGR_MONASTERY',41,4,0,138,87,1,563,173,3),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'UPGR_TRAINING_GROUNDS',42,4,1,301,108,10,160,190,3),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'UPGR_PORTAL_OF_GLORY',43,4,2,286,116,1,303,0,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'UPGR_GUARDHOUSE',(SELECT id FROM castle WHERE full_name='CASTLE'),(SELECT id from town_building WHERE full_name='GUARDHOUSE'),(SELECT id from town_build_anim where full_name = 'UPGR_GUARDHOUSE'),0,0,5,0,0,0,1000),
            ((SELECT MAX(id) + 2 FROM town_building),'UPGR_ARCHERS_TOWER',(SELECT id FROM castle WHERE full_name='CASTLE'),(SELECT id from town_building WHERE full_name='ARCHERS_TOWER'),(SELECT id from town_build_anim where full_name = 'UPGR_ARCHERS_TOWER'),5,0,5,0,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'UPGR_GRIFFIN_TOWER',(SELECT id FROM castle WHERE full_name='CASTLE'),(SELECT id from town_building WHERE full_name='GRIFFIN_TOWER'),(SELECT id from town_build_anim where full_name = 'UPGR_GRIFFIN_TOWER'),0,0,5,0,0,0,1000),
            ((SELECT MAX(id) + 4 FROM town_building),'UPGR_BARRACKS',(SELECT id FROM castle WHERE full_name='CASTLE'),(SELECT id from town_building WHERE full_name='BARRACKS'),(SELECT id from town_build_anim where full_name = 'UPGR_BARRACKS'),0,0,5,0,5,0,2000),
            ((SELECT MAX(id) + 5 FROM town_building),'UPGR_MONASTERY',(SELECT id FROM castle WHERE full_name='CASTLE'),(SELECT id from town_building WHERE full_name='MONASTERY'),(SELECT id from town_build_anim where full_name = 'UPGR_MONASTERY'),2,2,2,2,2,2,1000),
            ((SELECT MAX(id) + 6 FROM town_building),'UPGR_TRAINING_GROUNDS',(SELECT id FROM castle WHERE full_name='CASTLE'),(SELECT id from town_building WHERE full_name='TRAINING_GROUNDS'),(SELECT id from town_build_anim where full_name = 'UPGR_TRAINING_GROUNDS'),10,0,0,0,0,0,3000),
            ((SELECT MAX(id) + 7 FROM town_building),'UPGR_PORTAL_OF_GLORY',(SELECT id FROM castle WHERE full_name='CASTLE'),(SELECT id from town_building WHERE full_name='PORTAL_OF_GLORY'),(SELECT id from town_build_anim where full_name = 'UPGR_PORTAL_OF_GLORY'),0,10,0,10,10,10,20000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'LIGHTHOUSE',17,1,2,96,116,20,533,71,1),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'EMPTY_GRIFFIN_BASTION',18,2,1,0,0,0,0,0,1),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'GRIFFIN_BASTION',18,2,1,100,125,1,76,53,1),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'UPGR_GRIFFIN_BASTION',19,2,1,100,143,1,76,35,1),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'STABLES',21,2,0,120,58,1,384,193,2),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'BROTHERHOOD_OF_THE_SWORD',22,0,2,105,176,1,0,198,5);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'LIGHTHOUSE',(SELECT id FROM castle WHERE full_name='CASTLE'),null,(SELECT id from town_build_anim where full_name = 'LIGHTHOUSE'),0,0,10,0,0,0,2000),
            ((SELECT MAX(id) + 2 FROM town_building),'GRIFFIN_BASTION',(SELECT id FROM castle WHERE full_name='CASTLE'),null,(SELECT id from town_build_anim where full_name = 'EMPTY_GRIFFIN_BASTION'),0,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'STABLES',(SELECT id FROM castle WHERE full_name='CASTLE'),null,(SELECT id from town_build_anim where full_name = 'STABLES'),10,0,0,0,0,0,2000),
            ((SELECT MAX(id) + 4 FROM town_building),'BROTHERHOOD_OF_THE_SWORD',(SELECT id FROM castle WHERE full_name='CASTLE'),(SELECT id from town_building WHERE full_name='CASTLE_TAVERN'),(SELECT id from town_build_anim where full_name = 'BROTHERHOOD_OF_THE_SWORD'),5,0,0,0,0,0,500);

            insert into additional_build_anim (
            id, building_id, animation_id, existing_building_id
            ) values
            (COALESCE((SELECT MAX(id) FROM additional_build_anim), 0) + 1,(SELECT id FROM town_building WHERE full_name = 'GRIFFIN_TOWER'),(SELECT id FROM town_build_anim WHERE full_name = 'GRIFFIN_BASTION'),(SELECT id FROM town_building WHERE full_name = 'GRIFFIN_BASTION')),
            (COALESCE((SELECT MAX(id) FROM additional_build_anim), 0) + 2,(SELECT id FROM town_building WHERE full_name = 'UPGR_GRIFFIN_TOWER'),(SELECT id FROM town_build_anim WHERE full_name = 'UPGR_GRIFFIN_BASTION'),(SELECT id FROM town_building WHERE full_name = 'GRIFFIN_BASTION')),
            (COALESCE((SELECT MAX(id) FROM additional_build_anim), 0) + 3,(SELECT id FROM town_building WHERE full_name = 'CASTLE_SHIPYARD'),(SELECT id FROM town_build_anim WHERE full_name = 'CASTLE_CITADEL_SHIPYARD'),(SELECT id FROM town_building WHERE full_name = 'CASTLE_CITADEL')),
            (COALESCE((SELECT MAX(id) FROM additional_build_anim), 0) + 4,(SELECT id FROM town_building WHERE full_name = 'CASTLE_VILLAGE_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'CASTLE_WATERFALL'),null),
            (COALESCE((SELECT MAX(id) FROM additional_build_anim), 0) + 5,(SELECT id FROM town_building WHERE full_name = 'CASTLE_TOWN_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'CASTLE_WATERFALL'),null),
            (COALESCE((SELECT MAX(id) FROM additional_build_anim), 0) + 6,(SELECT id FROM town_building WHERE full_name = 'CASTLE_CITY_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'CASTLE_WATERFALL'),null),
            (COALESCE((SELECT MAX(id) FROM additional_build_anim), 0) + 7,(SELECT id FROM town_building WHERE full_name = 'CASTLE_CAPITOL'),(SELECT id FROM town_build_anim WHERE full_name = 'CASTLE_WATERFALL'),null);

            insert into building_prereq (
            id, building_id, prereq_id
            ) values
            (COALESCE((SELECT MAX(id) FROM building_prereq), 0) + 1,(SELECT id FROM town_building WHERE full_name = 'CASTLE_TOWN_HALL'),(SELECT id FROM town_building WHERE full_name = 'CASTLE_TAVERN')),
            (COALESCE((SELECT MAX(id) FROM building_prereq), 0) + 2,(SELECT id FROM town_building WHERE full_name = 'CASTLE_RESOURCE_SILO'),(SELECT id FROM town_building WHERE full_name = 'CASTLE_MARKETPLACE')),
            (COALESCE((SELECT MAX(id) FROM building_prereq), 0) + 3,(SELECT id FROM town_building WHERE full_name = 'CASTLE_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'CASTLE_MARKETPLACE')),
            (COALESCE((SELECT MAX(id) FROM building_prereq), 0) + 4,(SELECT id FROM town_building WHERE full_name = 'CASTLE_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'CASTLE_MAGE_GUILD_1')),
            (COALESCE((SELECT MAX(id) FROM building_prereq), 0) + 5,(SELECT id FROM town_building WHERE full_name = 'CASTLE_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'CASTLE_BLACKSMITH')),
            (COALESCE((SELECT MAX(id) FROM building_prereq), 0) + 6,(SELECT id FROM town_building WHERE full_name = 'CASTLE_CAPITOL'),(SELECT id FROM town_building WHERE full_name = 'CASTLE_CASTLE')),
            (COALESCE((SELECT MAX(id) FROM building_prereq), 0) + 7,(SELECT id FROM town_building WHERE full_name = 'LIGHTHOUSE'),(SELECT id FROM town_building WHERE full_name = 'CASTLE_SHIPYARD')),
            (COALESCE((SELECT MAX(id) FROM building_prereq), 0) + 8,(SELECT id FROM town_building WHERE full_name = 'GUARDHOUSE'),(SELECT id FROM town_building WHERE full_name = 'CASTLE_FORT')),
            (COALESCE((SELECT MAX(id) FROM building_prereq), 0) + 9,(SELECT id FROM town_building WHERE full_name = 'ARCHERS_TOWER'),(SELECT id FROM town_building WHERE full_name = 'GUARDHOUSE')),
            (COALESCE((SELECT MAX(id) FROM building_prereq), 0) + 10,(SELECT id FROM town_building WHERE full_name = 'BARRACKS'),(SELECT id FROM town_building WHERE full_name = 'GUARDHOUSE')),
            (COALESCE((SELECT MAX(id) FROM building_prereq), 0) + 11,(SELECT id FROM town_building WHERE full_name = 'BARRACKS'),(SELECT id FROM town_building WHERE full_name = 'CASTLE_BLACKSMITH')),
            (COALESCE((SELECT MAX(id) FROM building_prereq), 0) + 12,(SELECT id FROM town_building WHERE full_name = 'GRIFFIN_TOWER'),(SELECT id FROM town_building WHERE full_name = 'BARRACKS')),
            (COALESCE((SELECT MAX(id) FROM building_prereq), 0) + 13,(SELECT id FROM town_building WHERE full_name = 'GRIFFIN_BASTION'),(SELECT id FROM town_building WHERE full_name = 'GRIFFIN_TOWER')),
            (COALESCE((SELECT MAX(id) FROM building_prereq), 0) + 14,(SELECT id FROM town_building WHERE full_name = 'STABLES'),(SELECT id FROM town_building WHERE full_name = 'BARRACKS')),
            (COALESCE((SELECT MAX(id) FROM building_prereq), 0) + 15,(SELECT id FROM town_building WHERE full_name = 'MONASTERY'),(SELECT id FROM town_building WHERE full_name = 'BARRACKS')),
            (COALESCE((SELECT MAX(id) FROM building_prereq), 0) + 16,(SELECT id FROM town_building WHERE full_name = 'MONASTERY'),(SELECT id FROM town_building WHERE full_name = 'CASTLE_MAGE_GUILD_1')),
            (COALESCE((SELECT MAX(id) FROM building_prereq), 0) + 17,(SELECT id FROM town_building WHERE full_name = 'TRAINING_GROUNDS'),(SELECT id FROM town_building WHERE full_name = 'STABLES')),
            (COALESCE((SELECT MAX(id) FROM building_prereq), 0) + 18,(SELECT id FROM town_building WHERE full_name = 'PORTAL_OF_GLORY'),(SELECT id FROM town_building WHERE full_name = 'MONASTERY'));

            -- RAMPART
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'RAMPART_VILLAGE_HALL',10,0,0,215,124,1,565,216,5),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'SPIRIT_GUARDIAN',26,0,0,115,125,1,0,54,1),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'RAMPART_FORT',7,0,1,303,175,1,79,31,0),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'RAMPART_TAVERN',5,0,2,134,75,1,181,229,5),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'RAMPART_BLACKSMITH',16,0,3,177,89,1,558,105,1),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'RAMPART_MARKETPLACE',14,1,0,151,73,1,129,301,7),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'RAMPART_MAGE_GUILD_1',0,1,1,163,96,1,454,200,4),
            ((SELECT MAX(id) + 8 FROM town_build_anim),'RAMPART_VILLAGE_BUILDINGS',0,0,0,234,138,1,327,236,5),
            ((SELECT MAX(id) + 9 FROM town_build_anim),'RAMPART_TOWN_BUILDINGS',0,0,0,127,139,1,293,235,6),
            ((SELECT MAX(id) + 10 FROM town_build_anim),'RAMPART_CITY_BUILDINGS',0,0,0,201,74,1,295,191,3),
            ((SELECT MAX(id) + 11 FROM town_build_anim),'RAMPART_CAPITOL_BUILDINGS',0,0,0,111,83,1,260,171,4);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'RAMPART_VILLAGE_HALL',(SELECT id FROM castle WHERE full_name='RAMPART'),null,(SELECT id from town_build_anim where full_name = 'RAMPART_VILLAGE_HALL'),0,0,0,0,0,0,0),
            ((SELECT MAX(id) + 2 FROM town_building),'SPIRIT_GUARDIAN',(SELECT id FROM castle WHERE full_name='RAMPART'),null,(SELECT id from town_build_anim where full_name = 'SPIRIT_GUARDIAN'),0,0,0,0,0,0,0),
            ((SELECT MAX(id) + 3 FROM town_building),'RAMPART_FORT',(SELECT id FROM castle WHERE full_name='RAMPART'),null,(SELECT id from town_build_anim where full_name = 'RAMPART_FORT'),20,0,20,0,0,0,5000),
            ((SELECT MAX(id) + 4 FROM town_building),'RAMPART_TAVERN',(SELECT id FROM castle WHERE full_name='RAMPART'),null,(SELECT id from town_build_anim where full_name = 'RAMPART_TAVERN'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 5 FROM town_building),'RAMPART_BLACKSMITH',(SELECT id FROM castle WHERE full_name='RAMPART'),null,(SELECT id from town_build_anim where full_name = 'RAMPART_BLACKSMITH'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 6 FROM town_building),'RAMPART_MARKETPLACE',(SELECT id FROM castle WHERE full_name='RAMPART'),null,(SELECT id from town_build_anim where full_name = 'RAMPART_MARKETPLACE'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 7 FROM town_building),'RAMPART_MAGE_GUILD_1',(SELECT id FROM castle WHERE full_name='RAMPART'),null,(SELECT id from town_build_anim where full_name = 'RAMPART_MAGE_GUILD_1'),5,0,5,0,0,0,2000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'RAMPART_TOWN_HALL',11,0,0,241,149,1,538,187,5),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'RAMPART_RESOURCE_SILO',15,1,0,67,50,1,245,324,8),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'RAMPART_CITADEL',8,0,1,303,188,1,79,18,0),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'RAMPART_MAGE_GUILD_2',1,1,1,179,118,1,438,178,4);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'RAMPART_TOWN_HALL',(SELECT id FROM castle WHERE full_name = 'RAMPART'),(SELECT id FROM town_building WHERE full_name='RAMPART_VILLAGE_HALL'),(SELECT id from town_build_anim where full_name = 'RAMPART_TOWN_HALL'),0,0,0,0,0,0,2500),
            ((SELECT MAX(id) + 2 FROM town_building),'RAMPART_RESOURCE_SILO',(SELECT id FROM castle WHERE full_name = 'RAMPART'),null,(SELECT id from town_build_anim where full_name = 'RAMPART_RESOURCE_SILO'),0,0,5,0,0,0,5000),
            ((SELECT MAX(id) + 3 FROM town_building),'RAMPART_CITADEL',(SELECT id FROM castle WHERE full_name = 'RAMPART'),(SELECT id FROM town_building WHERE full_name='RAMPART_FORT'),(SELECT id from town_build_anim where full_name = 'RAMPART_CITADEL'),0,0,5,0,0,0,2500),
            ((SELECT MAX(id) + 4 FROM town_building),'RAMPART_MAGE_GUILD_2',(SELECT id FROM castle WHERE full_name = 'RAMPART'),(SELECT id FROM town_building WHERE full_name='RAMPART_MAGE_GUILD_1'),(SELECT id from town_build_anim where full_name = 'RAMPART_MAGE_GUILD_2'),5,4,5,4,4,4,5000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'RAMPART_CITY_HALL',12,0,0,262,180,1,538,187,5),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'RAMPART_CASTLE',9,0,1,303,188,1,79,18,0),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'RAMPART_MAGE_GUILD_3',2,1,1,199,143,1,418,153,4);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'RAMPART_CITY_HALL',(SELECT id FROM castle WHERE full_name = 'RAMPART'),(SELECT id FROM town_building WHERE full_name='RAMPART_TOWN_HALL'),(SELECT id from town_build_anim where full_name = 'RAMPART_CITY_HALL'),0,0,0,0,0,0,5000),
            ((SELECT MAX(id) + 2 FROM town_building),'RAMPART_CASTLE',(SELECT id FROM castle WHERE full_name = 'RAMPART'),(SELECT id FROM town_building WHERE full_name='RAMPART_CITADEL'),(SELECT id from town_build_anim where full_name = 'RAMPART_CASTLE'),10,0,10,0,0,0,5000),
            ((SELECT MAX(id) + 3 FROM town_building),'RAMPART_MAGE_GUILD_3',(SELECT id FROM castle WHERE full_name = 'RAMPART'),(SELECT id FROM town_building WHERE full_name='RAMPART_MAGE_GUILD_2'),(SELECT id from town_build_anim where full_name = 'RAMPART_MAGE_GUILD_3'),5,6,5,6,6,6,1000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'RAMPART_CAPITOL',13,0,0,266,180,1,534,187,5),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'RAMPART_MAGE_GUILD_4',3,1,1,211,167,1,406,129,4);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'RAMPART_CAPITOL',(SELECT id FROM castle WHERE full_name = 'RAMPART'),(SELECT id FROM town_building WHERE full_name='RAMPART_CITY_HALL'),(SELECT id from town_build_anim where full_name = 'RAMPART_CAPITOL'),0,0,0,0,0,0,10000),
            ((SELECT MAX(id) + 2 FROM town_building),'RAMPART_MAGE_GUILD_4',(SELECT id FROM castle WHERE full_name = 'RAMPART'),(SELECT id FROM town_building WHERE full_name='RAMPART_MAGE_GUILD_3'),(SELECT id from town_build_anim where full_name = 'RAMPART_MAGE_GUILD_4'),5,8,5,8,8,8,1000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'RAMPART_MAGE_GUILD_5',4,1,1,233,192,1,384,104,4);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'RAMPART_MAGE_GUILD_5',(SELECT id FROM castle WHERE full_name = 'RAMPART'),(SELECT id FROM town_building WHERE full_name='RAMPART_MAGE_GUILD_4'),(SELECT id from town_build_anim where full_name = 'RAMPART_MAGE_GUILD_5'),5,10,5,10,10,10,1000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'CENTAUR_STABLES',30,3,0,226,138,6,0,236,6),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'DWARF_COTTAGE',31,3,1,95,80,1,0,154,2),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'HOMESTEAD',32,3,2,132,99,1,668,101,2),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'ENCHANTED_SPRING',33,3,3,144,128,10,287,73,2),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'DENDROID_ARCHES',34,4,0,205,136,1,68,146,3),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'UNICORN_GLADE',35,4,1,219,109,1,362,90,1),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'DRAGON_CLIFFS',36,4,2,188,117,40,502,27,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'CENTAUR_STABLES',(SELECT id FROM castle WHERE full_name='RAMPART'),null,(SELECT id from town_build_anim where full_name = 'CENTAUR_STABLES'),10,0,0,0,0,0,500),
            ((SELECT MAX(id) + 2 FROM town_building),'DWARF_COTTAGE',(SELECT id FROM castle WHERE full_name='RAMPART'),null,(SELECT id from town_build_anim where full_name = 'DWARF_COTTAGE'),5,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'HOMESTEAD',(SELECT id FROM castle WHERE full_name='RAMPART'),null,(SELECT id from town_build_anim where full_name = 'HOMESTEAD'),10,0,0,0,0,0,1500),
            ((SELECT MAX(id) + 4 FROM town_building),'ENCHANTED_SPRING',(SELECT id FROM castle WHERE full_name='RAMPART'),null,(SELECT id from town_build_anim where full_name = 'ENCHANTED_SPRING'),0,0,0,0,10,0,2000),
            ((SELECT MAX(id) + 5 FROM town_building),'DENDROID_ARCHES',(SELECT id FROM castle WHERE full_name='RAMPART'),null,(SELECT id from town_build_anim where full_name = 'DENDROID_ARCHES'),0,0,0,0,0,0,2500),
            ((SELECT MAX(id) + 6 FROM town_building),'UNICORN_GLADE',(SELECT id FROM castle WHERE full_name='RAMPART'),null,(SELECT id from town_build_anim where full_name = 'UNICORN_GLADE'),5,0,5,0,0,10,4000),
            ((SELECT MAX(id) + 7 FROM town_building),'DRAGON_CLIFFS',(SELECT id FROM castle WHERE full_name='RAMPART'),null,(SELECT id from town_build_anim where full_name = 'DRAGON_CLIFFS'),0,0,30,0,20,0,10000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'UPGR_CENTAUR_STABLES',37,3,0,238,138,6,0,236,6),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'UPGR_DWARF_COTTAGE',38,3,1,95,91,1,0,143,2),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'UPGR_HOMESTEAD',39,3,2,135,99,1,665,101,2),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'UPGR_ENCHANTED_SPRING',40,3,3,144,173,10,287,28,2),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'UPGR_DENDROID_ARCHES',41,4,0,247,136,1,63,146,3),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'UPGR_UNICORN_GLADE',42,4,1,219,109,1,362,90,1),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'UPGR_DRAGON_CLIFFS',43,4,2,298,138,39,502,5,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'UPGR_CENTAUR_STABLES',(SELECT id FROM castle WHERE full_name='RAMPART'),(SELECT id from town_building WHERE full_name='CENTAUR_STABLES'),(SELECT id from town_build_anim where full_name = 'UPGR_CENTAUR_STABLES'),5,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 2 FROM town_building),'UPGR_DWARF_COTTAGE',(SELECT id FROM castle WHERE full_name='RAMPART'),(SELECT id from town_building WHERE full_name='DWARF_COTTAGE'),(SELECT id from town_build_anim where full_name = 'UPGR_DWARF_COTTAGE'),5,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'UPGR_HOMESTEAD',(SELECT id FROM castle WHERE full_name='RAMPART'),(SELECT id from town_building WHERE full_name='HOMESTEAD'),(SELECT id from town_build_anim where full_name = 'UPGR_HOMESTEAD'),10,0,0,0,0,0,1500),
            ((SELECT MAX(id) + 4 FROM town_building),'UPGR_ENCHANTED_SPRING',(SELECT id FROM castle WHERE full_name='RAMPART'),(SELECT id from town_building WHERE full_name='ENCHANTED_SPRING'),(SELECT id from town_build_anim where full_name = 'UPGR_ENCHANTED_SPRING'),0,0,0,0,5,0,2000),
            ((SELECT MAX(id) + 5 FROM town_building),'UPGR_DENDROID_ARCHES',(SELECT id FROM castle WHERE full_name='RAMPART'),(SELECT id from town_building WHERE full_name='DENDROID_ARCHES'),(SELECT id from town_build_anim where full_name = 'UPGR_DENDROID_ARCHES'),0,0,0,0,0,0,1500),
            ((SELECT MAX(id) + 6 FROM town_building),'UPGR_UNICORN_GLADE',(SELECT id FROM castle WHERE full_name='RAMPART'),(SELECT id from town_building WHERE full_name='UNICORN_GLADE'),(SELECT id from town_build_anim where full_name = 'UPGR_UNICORN_GLADE'),0,0,0,0,0,5,3000),
            ((SELECT MAX(id) + 7 FROM town_building),'UPGR_DRAGON_CLIFFS',(SELECT id FROM castle WHERE full_name='RAMPART'),(SELECT id from town_building WHERE full_name='DRAGON_CLIFFS'),(SELECT id from town_build_anim where full_name = 'UPGR_DRAGON_CLIFFS'),0,0,30,0,20,0,20000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'MYSTIC_POND',17,1,2,222,77,10,555,297,6),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'EMPTY_MINERS_GUILD',18,2,2,0,0,0,0,0,2),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'MINERS_GUILD',18,2,2,95,80,1,0,154,2),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'UPGR_MINERS_GUILD',19,2,2,95,91,1,0,143,2),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'FOUNTAIN_OF_FORTUNE',21,1,2,222,77,20,555,297,6),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'TREASURY',22,2,0,26,21,1,0,181,3),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'EMPTY_DENDROID_SAPLINGS',24,2,1,0,0,0,0,0,3),
            ((SELECT MAX(id) + 8 FROM town_build_anim),'DENDROID_SAPLINGS',24,2,1,226,140,1,47,142,3),
            ((SELECT MAX(id) + 9 FROM town_build_anim),'UPGR_DENDROID_SAPLINGS',25,2,1,263,140,1,47,142,3);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'MYSTIC_POND',(SELECT id FROM castle WHERE full_name='RAMPART'),null,(SELECT id from town_build_anim where full_name = 'MYSTIC_POND'),2,2,2,2,2,2,2000),
            ((SELECT MAX(id) + 2 FROM town_building),'MINERS_GUILD',(SELECT id FROM castle WHERE full_name='RAMPART'),null,(SELECT id from town_build_anim where full_name = 'EMPTY_MINERS_GUILD'),0,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'TREASURY',(SELECT id FROM castle WHERE full_name='RAMPART'),null,(SELECT id from town_build_anim where full_name = 'TREASURY'),5,0,10,0,0,0,5000),
            ((SELECT MAX(id) + 4 FROM town_building),'DENDROID_SAPLINGS',(SELECT id FROM castle WHERE full_name='RAMPART'),null,(SELECT id from town_build_anim where full_name = 'EMPTY_DENDROID_SAPLINGS'),0,0,0,0,0,0,2000);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'FOUNTAIN_OF_FORTUNE',(SELECT id FROM castle WHERE full_name='RAMPART'),(SELECT id FROM town_building WHERE full_name = 'MYSTIC_POND'),(SELECT id from town_build_anim where full_name = 'FOUNTAIN_OF_FORTUNE'),0,0,0,0,10,0,1500);

            insert into additional_build_anim (
            id, building_id, animation_id, existing_building_id
            ) values
            ((SELECT MAX(id) + 1 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'RAMPART_VILLAGE_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'RAMPART_VILLAGE_BUILDINGS'),null),
            ((SELECT MAX(id) + 2 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'RAMPART_TOWN_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'RAMPART_VILLAGE_BUILDINGS'),null),
            ((SELECT MAX(id) + 3 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'RAMPART_TOWN_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'RAMPART_TOWN_BUILDINGS'),null),
            ((SELECT MAX(id) + 4 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'RAMPART_CITY_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'RAMPART_VILLAGE_BUILDINGS'),null),
            ((SELECT MAX(id) + 5 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'RAMPART_CITY_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'RAMPART_TOWN_BUILDINGS'),null),
            ((SELECT MAX(id) + 6 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'RAMPART_CITY_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'RAMPART_CITY_BUILDINGS'),null),
            ((SELECT MAX(id) + 7 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'RAMPART_CAPITOL'),(SELECT id FROM town_build_anim WHERE full_name = 'RAMPART_VILLAGE_BUILDINGS'),null),
            ((SELECT MAX(id) + 8 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'RAMPART_CAPITOL'),(SELECT id FROM town_build_anim WHERE full_name = 'RAMPART_TOWN_BUILDINGS'),null),
            ((SELECT MAX(id) + 9 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'RAMPART_CAPITOL'),(SELECT id FROM town_build_anim WHERE full_name = 'RAMPART_CITY_BUILDINGS'),null),
            ((SELECT MAX(id) + 10 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'RAMPART_CAPITOL'),(SELECT id FROM town_build_anim WHERE full_name = 'RAMPART_CAPITOL_BUILDINGS'),null),
            ((SELECT MAX(id) + 11 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'DWARF_COTTAGE'),(SELECT id FROM town_build_anim WHERE full_name = 'MINERS_GUILD'),(SELECT id FROM town_building WHERE full_name = 'MINERS_GUILD')),
            ((SELECT MAX(id) + 12 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'UPGR_DWARF_COTTAGE'),(SELECT id FROM town_build_anim WHERE full_name = 'UPGR_MINERS_GUILD'),(SELECT id FROM town_building WHERE full_name = 'MINERS_GUILD')),
            ((SELECT MAX(id) + 13 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'DENDROID_ARCHES'),(SELECT id FROM town_build_anim WHERE full_name = 'DENDROID_SAPLINGS'),(SELECT id FROM town_building WHERE full_name = 'DENDROID_SAPLINGS')),
            ((SELECT MAX(id) + 14 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'UPGR_DENDROID_ARCHES'),(SELECT id FROM town_build_anim WHERE full_name = 'UPGR_DENDROID_SAPLINGS'),(SELECT id FROM town_building WHERE full_name = 'DENDROID_SAPLINGS'));

            insert into building_prereq (
            id, building_id, prereq_id
            ) values
            ((SELECT MAX(id) + 1 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'RAMPART_TOWN_HALL'),(SELECT id FROM town_building WHERE full_name = 'RAMPART_TAVERN')),
            ((SELECT MAX(id) + 2 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'RAMPART_RESOURCE_SILO'),(SELECT id FROM town_building WHERE full_name = 'RAMPART_MARKETPLACE')),
            ((SELECT MAX(id) + 3 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'RAMPART_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'RAMPART_MARKETPLACE')),
            ((SELECT MAX(id) + 4 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'RAMPART_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'RAMPART_MAGE_GUILD_1')),
            ((SELECT MAX(id) + 5 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'RAMPART_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'RAMPART_BLACKSMITH')),
            ((SELECT MAX(id) + 6 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'RAMPART_CAPITOL'),(SELECT id FROM town_building WHERE full_name = 'RAMPART_CASTLE')),
            ((SELECT MAX(id) + 7 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'CENTAUR_STABLES'),(SELECT id FROM town_building WHERE full_name = 'RAMPART_FORT')),
            ((SELECT MAX(id) + 8 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'DWARF_COTTAGE'),(SELECT id FROM town_building WHERE full_name = 'CENTAUR_STABLES')),
            ((SELECT MAX(id) + 9 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'HOMESTEAD'),(SELECT id FROM town_building WHERE full_name = 'CENTAUR_STABLES')),
            ((SELECT MAX(id) + 10 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'MINERS_GUILD'),(SELECT id FROM town_building WHERE full_name = 'DWARF_COTTAGE')),
            ((SELECT MAX(id) + 11 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'TREASURY'),(SELECT id FROM town_building WHERE full_name = 'MINERS_GUILD')),
            ((SELECT MAX(id) + 12 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'ENCHANTED_SPRING'),(SELECT id FROM town_building WHERE full_name = 'HOMESTEAD')),
            ((SELECT MAX(id) + 13 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'DENDROID_ARCHES'),(SELECT id FROM town_building WHERE full_name = 'HOMESTEAD')),
            ((SELECT MAX(id) + 14 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'DENDROID_SAPLINGS'),(SELECT id FROM town_building WHERE full_name = 'DENDROID_ARCHES')),
            ((SELECT MAX(id) + 15 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'UNICORN_GLADE'),(SELECT id FROM town_building WHERE full_name = 'ENCHANTED_SPRING')),
            ((SELECT MAX(id) + 16 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'UNICORN_GLADE'),(SELECT id FROM town_building WHERE full_name = 'DENDROID_ARCHES')),
            ((SELECT MAX(id) + 17 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'DRAGON_CLIFFS'),(SELECT id FROM town_building WHERE full_name = 'UNICORN_GLADE')),
            ((SELECT MAX(id) + 18 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'DRAGON_CLIFFS'),(SELECT id FROM town_building WHERE full_name = 'RAMPART_MAGE_GUILD_2')),
            ((SELECT MAX(id) + 19 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'UPGR_DRAGON_CLIFFS'),(SELECT id FROM town_building WHERE full_name = 'RAMPART_MAGE_GUILD_3'));

            -- TOWER
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'TOWER_VILLAGE_HALL',10,0,0,177,75,1,0,260,2),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'SKYSHIP',26,0,0,90,218,20,236,14,0),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'TOWER_FORT',7,0,1,107,278,1,304,0,1),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'TOWER_TAVERN',5,0,2,122,94,1,375,278,4),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'TOWER_BLACKSMITH',16,0,3,140,146,20,478,211,3),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'TOWER_MARKETPLACE',14,1,0,186,73,1,614,292,4),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'TOWER_MAGE_GUILD_1',0,1,1,171,152,1,597,82,1);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'TOWER_VILLAGE_HALL',(SELECT id FROM castle WHERE full_name='TOWER'),null,(SELECT id from town_build_anim where full_name = 'TOWER_VILLAGE_HALL'),0,0,0,0,0,0,0),
            ((SELECT MAX(id) + 2 FROM town_building),'SKYSHIP',(SELECT id FROM castle WHERE full_name='TOWER'),null,(SELECT id from town_build_anim where full_name = 'SKYSHIP'),0,0,0,0,0,0,0),
            ((SELECT MAX(id) + 3 FROM town_building),'TOWER_FORT',(SELECT id FROM castle WHERE full_name='TOWER'),null,(SELECT id from town_build_anim where full_name = 'TOWER_FORT'),20,0,20,0,0,0,5000),
            ((SELECT MAX(id) + 4 FROM town_building),'TOWER_TAVERN',(SELECT id FROM castle WHERE full_name='TOWER'),null,(SELECT id from town_build_anim where full_name = 'TOWER_TAVERN'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 5 FROM town_building),'TOWER_BLACKSMITH',(SELECT id FROM castle WHERE full_name='TOWER'),null,(SELECT id from town_build_anim where full_name = 'TOWER_BLACKSMITH'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 6 FROM town_building),'TOWER_MARKETPLACE',(SELECT id FROM castle WHERE full_name='TOWER'),null,(SELECT id from town_build_anim where full_name = 'TOWER_MARKETPLACE'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 7 FROM town_building),'TOWER_MAGE_GUILD_1',(SELECT id FROM castle WHERE full_name='TOWER'),null,(SELECT id from town_build_anim where full_name = 'TOWER_MAGE_GUILD_1'),5,0,5,0,0,0,2000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'TOWER_TOWN_HALL',11,0,0,264,139,1,0,220,2),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'TOWER_RESOURCE_SILO',15,1,0,27,94,1,763,214,5),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'TOWER_CITADEL',8,0,1,139,301,1,301,0,1),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'TOWER_MAGE_GUILD_2',1,1,1,178,169,1,593,65,1);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'TOWER_TOWN_HALL',(SELECT id FROM castle WHERE full_name = 'TOWER'),(SELECT id FROM town_building WHERE full_name='TOWER_VILLAGE_HALL'),(SELECT id from town_build_anim where full_name = 'TOWER_TOWN_HALL'),0,0,0,0,0,0,2500),
            ((SELECT MAX(id) + 2 FROM town_building),'TOWER_RESOURCE_SILO',(SELECT id FROM castle WHERE full_name = 'TOWER'),null,(SELECT id from town_build_anim where full_name = 'TOWER_RESOURCE_SILO'),0,0,5,0,0,0,5000),
            ((SELECT MAX(id) + 3 FROM town_building),'TOWER_CITADEL',(SELECT id FROM castle WHERE full_name = 'TOWER'),(SELECT id FROM town_building WHERE full_name='TOWER_FORT'),(SELECT id from town_build_anim where full_name = 'TOWER_CITADEL'),0,0,5,0,0,0,2500),
            ((SELECT MAX(id) + 4 FROM town_building),'TOWER_MAGE_GUILD_2',(SELECT id FROM castle WHERE full_name = 'TOWER'),(SELECT id FROM town_building WHERE full_name='TOWER_MAGE_GUILD_1'),(SELECT id from town_build_anim where full_name = 'TOWER_MAGE_GUILD_2'),5,4,5,4,4,4,5000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'TOWER_CITY_HALL',12,0,0,264,277,1,0,82,2),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'TOWER_CASTLE',9,0,1,139,301,1,301,0,1),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'TOWER_MAGE_GUILD_3',2,1,1,178,186,1,593,48,1);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'TOWER_CITY_HALL',(SELECT id FROM castle WHERE full_name = 'TOWER'),(SELECT id FROM town_building WHERE full_name='TOWER_TOWN_HALL'),(SELECT id from town_build_anim where full_name = 'TOWER_CITY_HALL'),0,0,0,0,0,0,5000),
            ((SELECT MAX(id) + 2 FROM town_building),'TOWER_CASTLE',(SELECT id FROM castle WHERE full_name = 'TOWER'),(SELECT id FROM town_building WHERE full_name='TOWER_CITADEL'),(SELECT id from town_build_anim where full_name = 'TOWER_CASTLE'),10,0,10,0,0,0,5000),
            ((SELECT MAX(id) + 3 FROM town_building),'TOWER_MAGE_GUILD_3',(SELECT id FROM castle WHERE full_name = 'TOWER'),(SELECT id FROM town_building WHERE full_name='TOWER_MAGE_GUILD_2'),(SELECT id from town_build_anim where full_name = 'TOWER_MAGE_GUILD_3'),5,6,5,6,6,6,1000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'TOWER_CAPITOL',13,0,0,264,277,1,0,82,2),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'TOWER_MAGE_GUILD_4',3,1,1,178,203,1,593,31,1);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'TOWER_CAPITOL',(SELECT id FROM castle WHERE full_name = 'TOWER'),(SELECT id FROM town_building WHERE full_name='TOWER_CITY_HALL'),(SELECT id from town_build_anim where full_name = 'TOWER_CAPITOL'),0,0,0,0,0,0,10000),
            ((SELECT MAX(id) + 2 FROM town_building),'TOWER_MAGE_GUILD_4',(SELECT id FROM castle WHERE full_name = 'TOWER'),(SELECT id FROM town_building WHERE full_name='TOWER_MAGE_GUILD_3'),(SELECT id from town_build_anim where full_name = 'TOWER_MAGE_GUILD_4'),5,8,5,8,8,8,1000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'TOWER_MAGE_GUILD_5',4,1,1,178,220,1,593,14,1);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'TOWER_MAGE_GUILD_5',(SELECT id FROM castle WHERE full_name = 'TOWER'),(SELECT id FROM town_building WHERE full_name='TOWER_MAGE_GUILD_4'),(SELECT id from town_build_anim where full_name = 'TOWER_MAGE_GUILD_5'),5,10,5,10,10,10,1000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'WORKSHOP',30,3,0,53,71,20,453,221,2),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'PARAPET',31,3,1,53,95,1,4,47,0),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'GOLEM_FACTORY',32,3,2,108,127,20,209,177,1),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'MAGE_TOWER',33,3,3,52,122,1,613,95,0),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'ALTAR_OF_WISHES',34,4,0,85,84,1,511,75,0),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'GOLDEN_PAVILION',35,4,1,105,66,1,681,208,3),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'CLOUD_TEMPLE',36,4,2,173,86,1,75,144,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'WORKSHOP',(SELECT id FROM castle WHERE full_name='TOWER'),null,(SELECT id from town_build_anim where full_name = 'WORKSHOP'),5,0,5,0,0,0,300),
            ((SELECT MAX(id) + 2 FROM town_building),'PARAPET',(SELECT id FROM castle WHERE full_name='TOWER'),null,(SELECT id from town_build_anim where full_name = 'PARAPET'),0,0,10,0,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'GOLEM_FACTORY',(SELECT id FROM castle WHERE full_name='TOWER'),null,(SELECT id from town_build_anim where full_name = 'GOLEM_FACTORY'),5,0,5,0,0,0,2000),
            ((SELECT MAX(id) + 4 FROM town_building),'MAGE_TOWER',(SELECT id FROM castle WHERE full_name='TOWER'),null,(SELECT id from town_build_anim where full_name = 'MAGE_TOWER'),5,5,5,5,5,5,2500),
            ((SELECT MAX(id) + 5 FROM town_building),'ALTAR_OF_WISHES',(SELECT id FROM castle WHERE full_name='TOWER'),null,(SELECT id from town_build_anim where full_name = 'ALTAR_OF_WISHES'),5,0,5,0,6,6,3000),
            ((SELECT MAX(id) + 6 FROM town_building),'GOLDEN_PAVILION',(SELECT id FROM castle WHERE full_name='TOWER'),null,(SELECT id from town_build_anim where full_name = 'GOLDEN_PAVILION'),5,2,5,2,2,2,4000),
            ((SELECT MAX(id) + 7 FROM town_building),'CLOUD_TEMPLE',(SELECT id FROM castle WHERE full_name='TOWER'),null,(SELECT id from town_build_anim where full_name = 'CLOUD_TEMPLE'),10,0,10,0,0,10,5000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'UPGR_WORKSHOP',37,3,0,64,71,20,446,221,2),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'UPGR_PARAPET',38,3,1,53,114,1,4,28,0),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'UPGR_GOLEM_FACTORY',39,3,2,108,127,20,209,177,1),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'UPGR_MAGE_TOWER',40,3,3,52,143,1,613,74,0),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'UPGR_ALTAR_OF_WISHES',41,4,0,85,151,1,511,8,0),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'UPGR_GOLDEN_PAVILION',42,4,1,105,117,1,681,157,3),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'UPGR_CLOUD_TEMPLE',43,4,2,173,139,1,75,91,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'UPGR_WORKSHOP',(SELECT id FROM castle WHERE full_name='TOWER'),(SELECT id from town_building WHERE full_name='WORKSHOP'),(SELECT id from town_build_anim where full_name = 'UPGR_WORKSHOP'),0,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 2 FROM town_building),'UPGR_PARAPET',(SELECT id FROM castle WHERE full_name='TOWER'),(SELECT id from town_building WHERE full_name='PARAPET'),(SELECT id from town_build_anim where full_name = 'UPGR_PARAPET'),0,0,5,0,0,0,1500),
            ((SELECT MAX(id) + 3 FROM town_building),'UPGR_GOLEM_FACTORY',(SELECT id FROM castle WHERE full_name='TOWER'),(SELECT id from town_building WHERE full_name='GOLEM_FACTORY'),(SELECT id from town_build_anim where full_name = 'UPGR_GOLEM_FACTORY'),5,5,5,0,0,0,2000),
            ((SELECT MAX(id) + 4 FROM town_building),'UPGR_MAGE_TOWER',(SELECT id FROM castle WHERE full_name='TOWER'),(SELECT id from town_building WHERE full_name='MAGE_TOWER'),(SELECT id from town_build_anim where full_name = 'UPGR_MAGE_TOWER'),5,0,0,0,0,0,2000),
            ((SELECT MAX(id) + 5 FROM town_building),'UPGR_ALTAR_OF_WISHES',(SELECT id FROM castle WHERE full_name='TOWER'),(SELECT id from town_building WHERE full_name='ALTAR_OF_WISHES'),(SELECT id from town_build_anim where full_name = 'UPGR_ALTAR_OF_WISHES'),5,0,0,0,0,0,2000),
            ((SELECT MAX(id) + 6 FROM town_building),'UPGR_GOLDEN_PAVILION',(SELECT id FROM castle WHERE full_name='TOWER'),(SELECT id from town_building WHERE full_name='GOLDEN_PAVILION'),(SELECT id from town_build_anim where full_name = 'UPGR_GOLDEN_PAVILION'),0,3,0,3,3,3,3000),
            ((SELECT MAX(id) + 7 FROM town_building),'UPGR_CLOUD_TEMPLE',(SELECT id FROM castle WHERE full_name='TOWER'),(SELECT id from town_building WHERE full_name='CLOUD_TEMPLE'),(SELECT id from town_build_anim where full_name = 'UPGR_CLOUD_TEMPLE'),5,0,5,0,0,30,25000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'TOWER_ARTIFACT_MERCHANTS',17,2,0,68,85,1,674,276,5),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'EMPTY_SCULPTORS_WINGS',18,2,2,0,0,0,0,0,0),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'SCULPTORS_WINGS',18,2,2,57,95,1,0,47,0),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'UPGR_SCULPTORS_WINGS',19,2,2,57,114,1,0,28,0),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'LOOKOUT_TOWER',21,2,1,45,191,1,409,82,1),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'LIBRARY',22,1,2,27,92,1,702,115,0),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'WALL_OF_KNOWLEDGE',23,1,3,178,45,1,593,189,2);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'TOWER_ARTIFACT_MERCHANTS',(SELECT id FROM castle WHERE full_name='TOWER'),null,(SELECT id from town_build_anim where full_name = 'TOWER_ARTIFACT_MERCHANTS'),0,0,0,0,0,0,10000),
            ((SELECT MAX(id) + 2 FROM town_building),'SCULPTORS_WINGS',(SELECT id FROM castle WHERE full_name='TOWER'),null,(SELECT id from town_build_anim where full_name = 'EMPTY_SCULPTORS_WINGS'),0,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'LOOKOUT_TOWER',(SELECT id FROM castle WHERE full_name='TOWER'),null,(SELECT id from town_build_anim where full_name = 'LOOKOUT_TOWER'),5,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 4 FROM town_building),'LIBRARY',(SELECT id FROM castle WHERE full_name='TOWER'),null,(SELECT id from town_build_anim where full_name = 'LIBRARY'),5,5,5,5,5,5,1500),
            ((SELECT MAX(id) + 5 FROM town_building),'WALL_OF_KNOWLEDGE',(SELECT id FROM castle WHERE full_name='TOWER'),null,(SELECT id from town_build_anim where full_name = 'WALL_OF_KNOWLEDGE'),0,0,5,0,0,0,1000);

            insert into additional_build_anim (
            id, building_id, animation_id, existing_building_id
            ) values
            ((SELECT MAX(id) + 1 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'PARAPET'),(SELECT id FROM town_build_anim WHERE full_name = 'SCULPTORS_WINGS'),(SELECT id FROM town_building WHERE full_name = 'SCULPTORS_WINGS')),
            ((SELECT MAX(id) + 2 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'UPGR_PARAPET'),(SELECT id FROM town_build_anim WHERE full_name = 'UPGR_SCULPTORS_WINGS'),(SELECT id FROM town_building WHERE full_name = 'SCULPTORS_WINGS'));

            insert into building_prereq (
            id, building_id, prereq_id
            ) values
            ((SELECT MAX(id) + 1 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'TOWER_TOWN_HALL'),(SELECT id FROM town_building WHERE full_name = 'TOWER_TAVERN')),
            ((SELECT MAX(id) + 2 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'TOWER_RESOURCE_SILO'),(SELECT id FROM town_building WHERE full_name = 'TOWER_MARKETPLACE')),
            ((SELECT MAX(id) + 3 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'TOWER_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'TOWER_MARKETPLACE')),
            ((SELECT MAX(id) + 4 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'TOWER_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'TOWER_MAGE_GUILD_1')),
            ((SELECT MAX(id) + 5 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'TOWER_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'TOWER_BLACKSMITH')),
            ((SELECT MAX(id) + 6 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'TOWER_CAPITOL'),(SELECT id FROM town_building WHERE full_name = 'TOWER_CASTLE')),
            ((SELECT MAX(id) + 7 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'LOOKOUT_TOWER'),(SELECT id FROM town_building WHERE full_name = 'TOWER_FORT')),
            ((SELECT MAX(id) + 8 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'TOWER_ARTIFACT_MERCHANTS'),(SELECT id FROM town_building WHERE full_name = 'TOWER_MARKETPLACE')),
            ((SELECT MAX(id) + 9 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'LIBRARY'),(SELECT id FROM town_building WHERE full_name = 'TOWER_MAGE_GUILD_1')),
            ((SELECT MAX(id) + 10 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'WALL_OF_KNOWLEDGE'),(SELECT id FROM town_building WHERE full_name = 'TOWER_MAGE_GUILD_1')),
            ((SELECT MAX(id) + 11 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'WORKSHOP'),(SELECT id FROM town_building WHERE full_name = 'TOWER_FORT')),
            ((SELECT MAX(id) + 12 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'PARAPET'),(SELECT id FROM town_building WHERE full_name = 'WORKSHOP')),
            ((SELECT MAX(id) + 13 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'SCULPTORS_WINGS'),(SELECT id FROM town_building WHERE full_name = 'PARAPET')),
            ((SELECT MAX(id) + 14 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'GOLEM_FACTORY'),(SELECT id FROM town_building WHERE full_name = 'WORKSHOP')),
            ((SELECT MAX(id) + 15 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'MAGE_TOWER'),(SELECT id FROM town_building WHERE full_name = 'PARAPET')),
            ((SELECT MAX(id) + 16 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'MAGE_TOWER'),(SELECT id FROM town_building WHERE full_name = 'GOLEM_FACTORY')),
            ((SELECT MAX(id) + 17 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'MAGE_TOWER'),(SELECT id FROM town_building WHERE full_name = 'TOWER_MAGE_GUILD_1')),
            ((SELECT MAX(id) + 18 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'UPGR_MAGE_TOWER'),(SELECT id FROM town_building WHERE full_name = 'LIBRARY')),
            ((SELECT MAX(id) + 19 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'ALTAR_OF_WISHES'),(SELECT id FROM town_building WHERE full_name = 'MAGE_TOWER')),
            ((SELECT MAX(id) + 20 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'GOLDEN_PAVILION'),(SELECT id FROM town_building WHERE full_name = 'MAGE_TOWER')),
            ((SELECT MAX(id) + 21 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'CLOUD_TEMPLE'),(SELECT id FROM town_building WHERE full_name = 'ALTAR_OF_WISHES')),
            ((SELECT MAX(id) + 22 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'CLOUD_TEMPLE'),(SELECT id FROM town_building WHERE full_name = 'GOLDEN_PAVILION'));

            -- INFERNO
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'INFERNO_VILLAGE_HALL',10,0,0,226,90,1,0,174,1),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'DEITY_OF_FIRE',26,0,0,256,182,1,24,10,0),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'INFERNO_FORT',7,0,1,250,238,10,222,44,1),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'INFERNO_TAVERN',5,0,2,94,70,1,105,219,2),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'INFERNO_BLACKSMITH',16,0,3,116,120,10,684,253,2),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'INFERNO_MARKETPLACE',14,1,0,144,72,1,511,301,3),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'INFERNO_MAGE_GUILD_1',0,1,1,124,94,10,667,127,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'INFERNO_VILLAGE_HALL',(SELECT id FROM castle WHERE full_name='INFERNO'),null,(SELECT id from town_build_anim where full_name = 'INFERNO_VILLAGE_HALL'),0,0,0,0,0,0,0),
            ((SELECT MAX(id) + 2 FROM town_building),'DEITY_OF_FIRE',(SELECT id FROM castle WHERE full_name='INFERNO'),null,(SELECT id from town_build_anim where full_name = 'DEITY_OF_FIRE'),0,0,0,0,0,0,0),
            ((SELECT MAX(id) + 3 FROM town_building),'INFERNO_FORT',(SELECT id FROM castle WHERE full_name='INFERNO'),null,(SELECT id from town_build_anim where full_name = 'INFERNO_FORT'),20,0,20,0,0,0,5000),
            ((SELECT MAX(id) + 4 FROM town_building),'INFERNO_TAVERN',(SELECT id FROM castle WHERE full_name='INFERNO'),null,(SELECT id from town_build_anim where full_name = 'INFERNO_TAVERN'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 5 FROM town_building),'INFERNO_BLACKSMITH',(SELECT id FROM castle WHERE full_name='INFERNO'),null,(SELECT id from town_build_anim where full_name = 'INFERNO_BLACKSMITH'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 6 FROM town_building),'INFERNO_MARKETPLACE',(SELECT id FROM castle WHERE full_name='INFERNO'),null,(SELECT id from town_build_anim where full_name = 'INFERNO_MARKETPLACE'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 7 FROM town_building),'INFERNO_MAGE_GUILD_1',(SELECT id FROM castle WHERE full_name='INFERNO'),null,(SELECT id from town_build_anim where full_name = 'INFERNO_MAGE_GUILD_1'),5,0,5,0,0,0,2000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'INFERNO_TOWN_HALL',11,0,0,226,90,1,0,174,1),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'INFERNO_RESOURCE_SILO',15,1,0,76,38,1,499,336,4),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'INFERNO_CITADEL',8,0,1,250,238,10,222,44,1),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'INFERNO_MAGE_GUILD_2',1,1,1,132,123,10,667,101,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'INFERNO_TOWN_HALL',(SELECT id FROM castle WHERE full_name = 'INFERNO'),(SELECT id FROM town_building WHERE full_name='INFERNO_VILLAGE_HALL'),(SELECT id from town_build_anim where full_name = 'INFERNO_TOWN_HALL'),0,0,0,0,0,0,2500),
            ((SELECT MAX(id) + 2 FROM town_building),'INFERNO_RESOURCE_SILO',(SELECT id FROM castle WHERE full_name = 'INFERNO'),null,(SELECT id from town_build_anim where full_name = 'INFERNO_RESOURCE_SILO'),0,0,5,0,0,0,5000),
            ((SELECT MAX(id) + 3 FROM town_building),'INFERNO_CITADEL',(SELECT id FROM castle WHERE full_name = 'INFERNO'),(SELECT id FROM town_building WHERE full_name='INFERNO_FORT'),(SELECT id from town_build_anim where full_name = 'INFERNO_CITADEL'),0,0,5,0,0,0,2500),
            ((SELECT MAX(id) + 4 FROM town_building),'INFERNO_MAGE_GUILD_2',(SELECT id FROM castle WHERE full_name = 'INFERNO'),(SELECT id FROM town_building WHERE full_name='INFERNO_MAGE_GUILD_1'),(SELECT id from town_build_anim where full_name = 'INFERNO_MAGE_GUILD_2'),5,4,5,4,4,4,5000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'INFERNO_CITY_HALL',12,0,0,226,90,1,0,174,1),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'INFERNO_CASTLE',9,0,1,250,263,10,222,18,1),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'INFERNO_MAGE_GUILD_3',2,1,1,132,141,10,667,83,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'INFERNO_CITY_HALL',(SELECT id FROM castle WHERE full_name = 'INFERNO'),(SELECT id FROM town_building WHERE full_name='INFERNO_TOWN_HALL'),(SELECT id from town_build_anim where full_name = 'INFERNO_CITY_HALL'),0,0,0,0,0,0,5000),
            ((SELECT MAX(id) + 2 FROM town_building),'INFERNO_CASTLE',(SELECT id FROM castle WHERE full_name = 'INFERNO'),(SELECT id FROM town_building WHERE full_name='INFERNO_CITADEL'),(SELECT id from town_build_anim where full_name = 'INFERNO_CASTLE'),10,0,10,0,0,0,5000),
            ((SELECT MAX(id) + 3 FROM town_building),'INFERNO_MAGE_GUILD_3',(SELECT id FROM castle WHERE full_name = 'INFERNO'),(SELECT id FROM town_building WHERE full_name='INFERNO_MAGE_GUILD_2'),(SELECT id from town_build_anim where full_name = 'INFERNO_MAGE_GUILD_3'),5,6,5,6,6,6,1000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'INFERNO_CAPITOL',13,0,0,226,132,1,0,131,1),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'INFERNO_MAGE_GUILD_4',3,1,1,132,168,10,667,56,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'INFERNO_CAPITOL',(SELECT id FROM castle WHERE full_name = 'INFERNO'),(SELECT id FROM town_building WHERE full_name='INFERNO_CITY_HALL'),(SELECT id from town_build_anim where full_name = 'INFERNO_CAPITOL'),0,0,0,0,0,0,10000),
            ((SELECT MAX(id) + 2 FROM town_building),'INFERNO_MAGE_GUILD_4',(SELECT id FROM castle WHERE full_name = 'INFERNO'),(SELECT id FROM town_building WHERE full_name='INFERNO_MAGE_GUILD_3'),(SELECT id from town_build_anim where full_name = 'INFERNO_MAGE_GUILD_4'),5,8,5,8,8,8,1000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'INFERNO_MAGE_GUILD_5',4,1,1,132,189,10,667,35,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'INFERNO_MAGE_GUILD_5',(SELECT id FROM castle WHERE full_name = 'INFERNO'),(SELECT id FROM town_building WHERE full_name='INFERNO_MAGE_GUILD_4'),(SELECT id from town_build_anim where full_name = 'INFERNO_MAGE_GUILD_5'),5,10,5,10,10,10,1000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'IMP_CRUCIBLE',30,3,0,88,62,5,614,256,1),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'HALL_OF_SINS',31,3,1,138,126,10,187,248,3),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'KENNELS',32,3,2,110,38,1,9,325,2),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'DEMON_GATE',33,3,3,96,92,1,414,204,1),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'HELL_HOLE',34,4,0,192,56,1,359,296,2),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'FIRE_LAKE',35,4,1,280,24,1,220,350,4),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'FORSAKEN_PALACE',36,4,2,188,94,1,420,152,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'IMP_CRUCIBLE',(SELECT id FROM castle WHERE full_name='INFERNO'),null,(SELECT id from town_build_anim where full_name = 'IMP_CRUCIBLE'),5,0,5,0,0,0,300),
            ((SELECT MAX(id) + 2 FROM town_building),'HALL_OF_SINS',(SELECT id FROM castle WHERE full_name='INFERNO'),null,(SELECT id from town_build_anim where full_name = 'HALL_OF_SINS'),0,0,5,0,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'KENNELS',(SELECT id FROM castle WHERE full_name='INFERNO'),null,(SELECT id from town_build_anim where full_name = 'KENNELS'),10,0,0,0,0,0,1500),
            ((SELECT MAX(id) + 4 FROM town_building),'DEMON_GATE',(SELECT id FROM castle WHERE full_name='INFERNO'),null,(SELECT id from town_build_anim where full_name = 'DEMON_GATE'),5,0,5,0,0,0,2000),
            ((SELECT MAX(id) + 5 FROM town_building),'HELL_HOLE',(SELECT id FROM castle WHERE full_name='INFERNO'),null,(SELECT id from town_build_anim where full_name = 'HELL_HOLE'),0,0,0,0,0,0,3000),
            ((SELECT MAX(id) + 6 FROM town_building),'FIRE_LAKE',(SELECT id FROM castle WHERE full_name='INFERNO'),null,(SELECT id from town_build_anim where full_name = 'FIRE_LAKE'),0,3,10,3,0,3,4000),
            ((SELECT MAX(id) + 7 FROM town_building),'FORSAKEN_PALACE',(SELECT id FROM castle WHERE full_name='INFERNO'),null,(SELECT id from town_build_anim where full_name = 'FORSAKEN_PALACE'),10,20,10,0,0,0,15000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'UPGR_IMP_CRUCIBLE',37,3,0,88,97,5,614,221,1),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'UPGR_HALL_OF_SINS',38,3,1,138,162,10,187,212,3),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'UPGR_KENNELS',39,3,2,136,90,1,9,273,2),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'UPGR_DEMON_GATE',40,3,3,96,92,1,414,203,1),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'UPGR_HELL_HOLE',41,4,0,191,107,1,359,244,2),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'UPGR_FIRE_LAKE',42,4,1,280,91,1,220,282,4),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'UPGR_FORSAKEN_PALACE',43,4,2,188,142,1,420,105,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'UPGR_IMP_CRUCIBLE',(SELECT id FROM castle WHERE full_name='INFERNO'),(SELECT id from town_building WHERE full_name='IMP_CRUCIBLE'),(SELECT id from town_build_anim where full_name = 'UPGR_IMP_CRUCIBLE'),0,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 2 FROM town_building),'UPGR_HALL_OF_SINS',(SELECT id FROM castle WHERE full_name='INFERNO'),(SELECT id from town_building WHERE full_name='HALL_OF_SINS'),(SELECT id from town_build_anim where full_name = 'UPGR_HALL_OF_SINS'),0,5,0,0,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'UPGR_KENNELS',(SELECT id FROM castle WHERE full_name='INFERNO'),(SELECT id from town_building WHERE full_name='KENNELS'),(SELECT id from town_build_anim where full_name = 'UPGR_KENNELS'),0,0,0,5,0,0,1500),
            ((SELECT MAX(id) + 4 FROM town_building),'UPGR_DEMON_GATE',(SELECT id FROM castle WHERE full_name='INFERNO'),(SELECT id from town_building WHERE full_name='DEMON_GATE'),(SELECT id from town_build_anim where full_name = 'UPGR_DEMON_GATE'),5,0,5,0,0,0,2000),
            ((SELECT MAX(id) + 5 FROM town_building),'UPGR_HELL_HOLE',(SELECT id FROM castle WHERE full_name='INFERNO'),(SELECT id from town_building WHERE full_name='HELL_HOLE'),(SELECT id from town_build_anim where full_name = 'UPGR_HELL_HOLE'),0,5,0,5,0,0,3000),
            ((SELECT MAX(id) + 6 FROM town_building),'UPGR_FIRE_LAKE',(SELECT id FROM castle WHERE full_name='INFERNO'),(SELECT id from town_building WHERE full_name='FIRE_LAKE'),(SELECT id from town_build_anim where full_name = 'UPGR_FIRE_LAKE'),0,5,5,5,0,5,3000),
            ((SELECT MAX(id) + 7 FROM town_building),'UPGR_FORSAKEN_PALACE',(SELECT id FROM castle WHERE full_name='INFERNO'),(SELECT id from town_building WHERE full_name='FORSAKEN_PALACE'),(SELECT id from town_build_anim where full_name = 'UPGR_FORSAKEN_PALACE'),5,20,5,0,0,0,20000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'EMPTY_BIRTHING_POOL',18,2,1,0,0,0,0,0,1),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'BIRTHING_POOL',18,2,1,88,62,5,614,256,1),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'UPGR_BIRTHING_POOL',19,2,1,88,97,5,614,221,1),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'BRIMSTONE_STORMCLOUDS',21,1,3,336,64,10,297,0,0),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'CASTLE_GATE',22,2,0,64,64,10,227,174,2),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'ORDER_OF_FIRE',23,1,2,84,124,1,593,104,0),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'EMPTY_CAGES',24,2,2,0,0,0,0,0,2),
            ((SELECT MAX(id) + 8 FROM town_build_anim),'CAGES',24,2,2,149,62,1,9,301,2),
            ((SELECT MAX(id) + 9 FROM town_build_anim),'UPGR_CAGES',25,2,2,150,90,1,9,273,2);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'BIRTHING_POOL',(SELECT id FROM castle WHERE full_name='INFERNO'),null,(SELECT id from town_build_anim where full_name = 'EMPTY_BIRTHING_POOL'),0,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 2 FROM town_building),'BRIMSTONE_STORMCLOUDS',(SELECT id FROM castle WHERE full_name='INFERNO'),null,(SELECT id from town_build_anim where full_name = 'BRIMSTONE_STORMCLOUDS'),0,0,0,5,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'CASTLE_GATE',(SELECT id FROM castle WHERE full_name='INFERNO'),null,(SELECT id from town_build_anim where full_name = 'CASTLE_GATE'),5,0,5,0,0,0,10000),
            ((SELECT MAX(id) + 4 FROM town_building),'ORDER_OF_FIRE',(SELECT id FROM castle WHERE full_name='INFERNO'),null,(SELECT id from town_build_anim where full_name = 'ORDER_OF_FIRE'),5,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 5 FROM town_building),'CAGES',(SELECT id FROM castle WHERE full_name='INFERNO'),null,(SELECT id from town_build_anim where full_name = 'EMPTY_CAGES'),0,0,0,0,0,0,1000);

            insert into additional_build_anim (
            id, building_id, animation_id, existing_building_id
            ) values
            ((SELECT MAX(id) + 1 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'IMP_CRUCIBLE'),(SELECT id FROM town_build_anim WHERE full_name = 'BIRTHING_POOL'),(SELECT id FROM town_building WHERE full_name = 'BIRTHING_POOL')),
            ((SELECT MAX(id) + 2 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'UPGR_IMP_CRUCIBLE'),(SELECT id FROM town_build_anim WHERE full_name = 'UPGR_BIRTHING_POOL'),(SELECT id FROM town_building WHERE full_name = 'BIRTHING_POOL')),
            ((SELECT MAX(id) + 3 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'KENNELS'),(SELECT id FROM town_build_anim WHERE full_name = 'CAGES'),(SELECT id FROM town_building WHERE full_name = 'CAGES')),
            ((SELECT MAX(id) + 4 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'UPGR_KENNELS'),(SELECT id FROM town_build_anim WHERE full_name = 'UPGR_CAGES'),(SELECT id FROM town_building WHERE full_name = 'CAGES'));

            insert into building_prereq (
            id, building_id, prereq_id
            ) values
            ((SELECT MAX(id) + 1 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'INFERNO_TOWN_HALL'),(SELECT id FROM town_building WHERE full_name = 'INFERNO_TAVERN')),
            ((SELECT MAX(id) + 2 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'INFERNO_RESOURCE_SILO'),(SELECT id FROM town_building WHERE full_name = 'INFERNO_MARKETPLACE')),
            ((SELECT MAX(id) + 3 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'INFERNO_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'INFERNO_MARKETPLACE')),
            ((SELECT MAX(id) + 4 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'INFERNO_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'INFERNO_MAGE_GUILD_1')),
            ((SELECT MAX(id) + 5 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'INFERNO_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'INFERNO_BLACKSMITH')),
            ((SELECT MAX(id) + 6 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'INFERNO_CAPITOL'),(SELECT id FROM town_building WHERE full_name = 'INFERNO_CASTLE')),
            ((SELECT MAX(id) + 7 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'BRIMSTONE_STORMCLOUDS'),(SELECT id FROM town_building WHERE full_name = 'INFERNO_FORT')),
            ((SELECT MAX(id) + 8 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'CASTLE_GATE'),(SELECT id FROM town_building WHERE full_name = 'INFERNO_CITADEL')),
            ((SELECT MAX(id) + 9 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'IMP_CRUCIBLE'),(SELECT id FROM town_building WHERE full_name = 'INFERNO_FORT')),
            ((SELECT MAX(id) + 10 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'BIRTHING_POOL'),(SELECT id FROM town_building WHERE full_name = 'IMP_CRUCIBLE')),
            ((SELECT MAX(id) + 11 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'HALL_OF_SINS'),(SELECT id FROM town_building WHERE full_name = 'IMP_CRUCIBLE')),
            ((SELECT MAX(id) + 12 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'KENNELS'),(SELECT id FROM town_building WHERE full_name = 'IMP_CRUCIBLE')),
            ((SELECT MAX(id) + 13 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'CAGES'),(SELECT id FROM town_building WHERE full_name = 'KENNELS')),
            ((SELECT MAX(id) + 14 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'ORDER_OF_FIRE'),(SELECT id FROM town_building WHERE full_name = 'INFERNO_MAGE_GUILD_1')),
            ((SELECT MAX(id) + 15 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'DEMON_GATE'),(SELECT id FROM town_building WHERE full_name = 'HALL_OF_SINS')),
            ((SELECT MAX(id) + 16 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'HELL_HOLE'),(SELECT id FROM town_building WHERE full_name = 'DEMON_GATE')),
            ((SELECT MAX(id) + 17 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'FIRE_LAKE'),(SELECT id FROM town_building WHERE full_name = 'DEMON_GATE')),
            ((SELECT MAX(id) + 18 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'FIRE_LAKE'),(SELECT id FROM town_building WHERE full_name = 'INFERNO_MAGE_GUILD_1')),
            ((SELECT MAX(id) + 19 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'UPGR_HELL_HOLE'),(SELECT id FROM town_building WHERE full_name = 'INFERNO_MAGE_GUILD_2')),
            ((SELECT MAX(id) + 20 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'FORSAKEN_PALACE'),(SELECT id FROM town_building WHERE full_name = 'HELL_HOLE')),
            ((SELECT MAX(id) + 21 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'FORSAKEN_PALACE'),(SELECT id FROM town_building WHERE full_name = 'FIRE_LAKE'));

            -- NECROPOLIS
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'NECROPOLIS_VILLAGE_HALL',10,0,0,174,158,1,468,76,0),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'SOUL_PRISON',26,0,0,128,178,5,410,88,2),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'NECROPOLIS_FORT',7,0,1,200,196,1,138,66,1),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'NECROPOLIS_TAVERN',5,0,2,199,68,1,508,189,1),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'NECROPOLIS_BLACKSMITH',16,0,3,114,55,10,382,252,4),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'NECROPOLIS_MARKETPLACE',14,1,0,99,48,1,347,215,3),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'NECROPOLIS_MAGE_GUILD_1',0,1,1,144,93,1,341,116,1),
            ((SELECT MAX(id) + 8 FROM town_build_anim),'NECROPOLIS_SHIPYARD',6,1,3,183,109,10,617,265,4),
            ((SELECT MAX(id) + 9 FROM town_build_anim),'NECROPOLIS_VILLAGE_BUILDINGS',0,0,0,732,95,1,25,279,5),
            ((SELECT MAX(id) + 10 FROM town_build_anim),'NECROPOLIS_TOWN_BUILDINGS',0,0,0,177,106,1,0,251,5),
            ((SELECT MAX(id) + 11 FROM town_build_anim),'NECROPOLIS_CITY_BUILDINGS',0,0,0,87,62,1,321,255,5),
            ((SELECT MAX(id) + 12 FROM town_build_anim),'NECROPOLIS_CAPITOL_BUILDINGS',0,0,0,181,51,1,475,257,5);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'NECROPOLIS_VILLAGE_HALL',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),null,(SELECT id from town_build_anim where full_name = 'NECROPOLIS_VILLAGE_HALL'),0,0,0,0,0,0,0),
            ((SELECT MAX(id) + 2 FROM town_building),'SOUL_PRISON',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),null,(SELECT id from town_build_anim where full_name = 'SOUL_PRISON'),0,0,0,0,0,0,0),
            ((SELECT MAX(id) + 3 FROM town_building),'NECROPOLIS_FORT',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),null,(SELECT id from town_build_anim where full_name = 'NECROPOLIS_FORT'),20,0,20,0,0,0,5000),
            ((SELECT MAX(id) + 4 FROM town_building),'NECROPOLIS_TAVERN',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),null,(SELECT id from town_build_anim where full_name = 'NECROPOLIS_TAVERN'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 5 FROM town_building),'NECROPOLIS_BLACKSMITH',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),null,(SELECT id from town_build_anim where full_name = 'NECROPOLIS_BLACKSMITH'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 6 FROM town_building),'NECROPOLIS_MARKETPLACE',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),null,(SELECT id from town_build_anim where full_name = 'NECROPOLIS_MARKETPLACE'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 7 FROM town_building),'NECROPOLIS_MAGE_GUILD_1',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),null,(SELECT id from town_build_anim where full_name = 'NECROPOLIS_MAGE_GUILD_1'),5,0,5,0,0,0,2000),
            ((SELECT MAX(id) + 8 FROM town_building),'NECROPOLIS_SHIPYARD',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),null,(SELECT id from town_build_anim where full_name = 'NECROPOLIS_SHIPYARD'),20,0,0,0,0,0,2000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'NECROPOLIS_TOWN_HALL',11,0,0,155,176,1,482,56,0),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'NECROPOLIS_RESOURCE_SILO',15,1,0,93,77,1,276,185,2),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'NECROPOLIS_CITADEL',8,0,1,237,196,1,139,66,1),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'NECROPOLIS_MAGE_GUILD_2',1,1,1,144,112,1,341,97,1);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'NECROPOLIS_TOWN_HALL',(SELECT id FROM castle WHERE full_name = 'NECROPOLIS'),(SELECT id FROM town_building WHERE full_name='NECROPOLIS_VILLAGE_HALL'),(SELECT id from town_build_anim where full_name = 'NECROPOLIS_TOWN_HALL'),0,0,0,0,0,0,2500),
            ((SELECT MAX(id) + 2 FROM town_building),'NECROPOLIS_RESOURCE_SILO',(SELECT id FROM castle WHERE full_name = 'NECROPOLIS'),null,(SELECT id from town_build_anim where full_name = 'NECROPOLIS_RESOURCE_SILO'),0,0,5,0,0,0,5000),
            ((SELECT MAX(id) + 3 FROM town_building),'NECROPOLIS_CITADEL',(SELECT id FROM castle WHERE full_name = 'NECROPOLIS'),(SELECT id FROM town_building WHERE full_name='NECROPOLIS_FORT'),(SELECT id from town_build_anim where full_name = 'NECROPOLIS_CITADEL'),0,0,5,0,0,0,2500),
            ((SELECT MAX(id) + 4 FROM town_building),'NECROPOLIS_MAGE_GUILD_2',(SELECT id FROM castle WHERE full_name = 'NECROPOLIS'),(SELECT id FROM town_building WHERE full_name='NECROPOLIS_MAGE_GUILD_1'),(SELECT id from town_build_anim where full_name = 'NECROPOLIS_MAGE_GUILD_2'),5,4,5,4,4,4,5000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'NECROPOLIS_CITY_HALL',12,0,0,158,207,1,478,26,0),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'NECROPOLIS_CASTLE',9,0,1,342,251,1,34,18,1),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'NECROPOLIS_MAGE_GUILD_3',2,1,1,144,131,1,341,78,1);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'NECROPOLIS_CITY_HALL',(SELECT id FROM castle WHERE full_name = 'NECROPOLIS'),(SELECT id FROM town_building WHERE full_name='NECROPOLIS_TOWN_HALL'),(SELECT id from town_build_anim where full_name = 'NECROPOLIS_CITY_HALL'),0,0,0,0,0,0,5000),
            ((SELECT MAX(id) + 2 FROM town_building),'NECROPOLIS_CASTLE',(SELECT id FROM castle WHERE full_name = 'NECROPOLIS'),(SELECT id FROM town_building WHERE full_name='NECROPOLIS_CITADEL'),(SELECT id from town_build_anim where full_name = 'NECROPOLIS_CASTLE'),10,0,10,0,0,0,5000),
            ((SELECT MAX(id) + 3 FROM town_building),'NECROPOLIS_MAGE_GUILD_3',(SELECT id FROM castle WHERE full_name = 'NECROPOLIS'),(SELECT id FROM town_building WHERE full_name='NECROPOLIS_MAGE_GUILD_2'),(SELECT id from town_build_anim where full_name = 'NECROPOLIS_MAGE_GUILD_3'),5,6,5,6,6,6,1000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'NECROPOLIS_CAPITOL',13,0,0,154,205,1,481,26,0),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'NECROPOLIS_MAGE_GUILD_4',3,1,1,145,147,1,340,62,1);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'NECROPOLIS_CAPITOL',(SELECT id FROM castle WHERE full_name = 'NECROPOLIS'),(SELECT id FROM town_building WHERE full_name='NECROPOLIS_CITY_HALL'),(SELECT id from town_build_anim where full_name = 'NECROPOLIS_CAPITOL'),0,0,0,0,0,0,10000),
            ((SELECT MAX(id) + 2 FROM town_building),'NECROPOLIS_MAGE_GUILD_4',(SELECT id FROM castle WHERE full_name = 'NECROPOLIS'),(SELECT id FROM town_building WHERE full_name='NECROPOLIS_MAGE_GUILD_3'),(SELECT id from town_build_anim where full_name = 'NECROPOLIS_MAGE_GUILD_4'),5,8,5,8,8,8,1000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'NECROPOLIS_MAGE_GUILD_5',4,1,1,142,174,1,343,35,1);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'NECROPOLIS_MAGE_GUILD_5',(SELECT id FROM castle WHERE full_name = 'NECROPOLIS'),(SELECT id FROM town_building WHERE full_name='NECROPOLIS_MAGE_GUILD_4'),(SELECT id from town_build_anim where full_name = 'NECROPOLIS_MAGE_GUILD_5'),5,10,5,10,10,10,1000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'CURSED_TEMPLE',30,3,0,183,108,1,80,222,4),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'GRAVEYARD',31,3,1,132,74,1,502,223,3),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'TOMB_OF_SOULS',32,3,2,144,187,1,0,187,6),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'ESTATE',33,3,3,188,64,1,607,211,2),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'MAUSOLEUM',34,4,0,129,85,1,206,207,3),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'HALL_OF_DARKNESS',35,4,1,125,192,1,0,31,0),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'DRAGON_VAULT',36,4,2,137,164,1,663,25,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'CURSED_TEMPLE',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),null,(SELECT id from town_build_anim where full_name = 'CURSED_TEMPLE'),5,0,5,0,0,0,400),
            ((SELECT MAX(id) + 2 FROM town_building),'GRAVEYARD',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),null,(SELECT id from town_build_anim where full_name = 'GRAVEYARD'),0,0,5,0,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'TOMB_OF_SOULS',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),null,(SELECT id from town_build_anim where full_name = 'TOMB_OF_SOULS'),5,0,5,0,0,0,1500),
            ((SELECT MAX(id) + 4 FROM town_building),'ESTATE',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),null,(SELECT id from town_build_anim where full_name = 'ESTATE'),5,0,5,0,0,0,2000),
            ((SELECT MAX(id) + 5 FROM town_building),'MAUSOLEUM',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),null,(SELECT id from town_build_anim where full_name = 'MAUSOLEUM'),0,0,10,10,0,0,2000),
            ((SELECT MAX(id) + 6 FROM town_building),'HALL_OF_DARKNESS',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),null,(SELECT id from town_build_anim where full_name = 'HALL_OF_DARKNESS'),10,0,10,0,0,0,6000),
            ((SELECT MAX(id) + 7 FROM town_building),'DRAGON_VAULT',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),null,(SELECT id from town_build_anim where full_name = 'DRAGON_VAULT'),5,5,5,5,5,5,10000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'UPGR_CURSED_TEMPLE',37,3,0,193,117,1,64,222,4),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'UPGR_GRAVEYARD',38,3,1,134,77,1,498,224,3),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'UPGR_TOMB_OF_SOULS',39,3,2,147,195,1,0,179,6),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'UPGR_ESTATE',40,3,3,183,83,1,615,193,2),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'UPGR_MAUSOLEUM',41,4,0,114,120,1,222,171,3),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'UPGR_HALL_OF_DARKNESS',42,4,1,125,193,1,0,30,0),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'UPGR_DRAGON_VAULT',43,4,2,138,168,1,662,23,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'UPGR_CURSED_TEMPLE',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),(SELECT id from town_building WHERE full_name='CURSED_TEMPLE'),(SELECT id from town_build_anim where full_name = 'UPGR_CURSED_TEMPLE'),5,0,5,0,0,0,1000),
            ((SELECT MAX(id) + 2 FROM town_building),'UPGR_GRAVEYARD',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),(SELECT id from town_building WHERE full_name='GRAVEYARD'),(SELECT id from town_build_anim where full_name = 'UPGR_GRAVEYARD'),5,0,5,0,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'UPGR_TOMB_OF_SOULS',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),(SELECT id from town_building WHERE full_name='TOMB_OF_SOULS'),(SELECT id from town_build_anim where full_name = 'UPGR_TOMB_OF_SOULS'),0,5,0,0,0,0,1500),
            ((SELECT MAX(id) + 4 FROM town_building),'UPGR_ESTATE',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),(SELECT id from town_building WHERE full_name='ESTATE'),(SELECT id from town_build_anim where full_name = 'UPGR_ESTATE'),5,0,0,0,10,10,2000),
            ((SELECT MAX(id) + 5 FROM town_building),'UPGR_MAUSOLEUM',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),(SELECT id from town_building WHERE full_name='MAUSOLEUM'),(SELECT id from town_build_anim where full_name = 'UPGR_MAUSOLEUM'),0,0,5,5,0,0,2000),
            ((SELECT MAX(id) + 6 FROM town_building),'UPGR_HALL_OF_DARKNESS',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),(SELECT id from town_building WHERE full_name='HALL_OF_DARKNESS'),(SELECT id from town_build_anim where full_name = 'UPGR_HALL_OF_DARKNESS'),5,2,5,2,2,2,3000),
            ((SELECT MAX(id) + 7 FROM town_building),'UPGR_DRAGON_VAULT',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),(SELECT id from town_building WHERE full_name='DRAGON_VAULT'),(SELECT id from town_build_anim where full_name = 'UPGR_DRAGON_VAULT'),5,20,5,0,0,0,15000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'COVER_OF_DARKNESS',17,2,0,216,231,20,18,0,0),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'EMPTY_UNEARTHED_GRAVES',18,2,2,0,0,0,0,0,4),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'UNEARTHED_GRAVES',18,2,2,196,130,1,80,222,4),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'UPGR_UNEARTHED_GRAVES',19,2,2,212,130,1,64,222,4),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'NECROMANCY_AMPLIFIER',21,1,2,79,150,9,307,61,0),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'SKELETON_TRANSFORMER',22,2,1,87,37,1,247,275,4);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'COVER_OF_DARKNESS',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),null,(SELECT id from town_build_anim where full_name = 'COVER_OF_DARKNESS'),0,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 2 FROM town_building),'UNEARTHED_GRAVES',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),null,(SELECT id from town_build_anim where full_name = 'EMPTY_UNEARTHED_GRAVES'),0,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'NECROMANCY_AMPLIFIER',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),null,(SELECT id from town_build_anim where full_name = 'NECROMANCY_AMPLIFIER'),0,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 4 FROM town_building),'SKELETON_TRANSFORMER',(SELECT id FROM castle WHERE full_name='NECROPOLIS'),null,(SELECT id from town_build_anim where full_name = 'SKELETON_TRANSFORMER'),0,0,0,0,0,0,1000);

            insert into additional_build_anim (
            id, building_id, animation_id, existing_building_id
            ) values
            ((SELECT MAX(id) + 1 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_VILLAGE_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'NECROPOLIS_VILLAGE_BUILDINGS'),null),
            ((SELECT MAX(id) + 2 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_TOWN_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'NECROPOLIS_VILLAGE_BUILDINGS'),null),
            ((SELECT MAX(id) + 3 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_TOWN_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'NECROPOLIS_TOWN_BUILDINGS'),null),
            ((SELECT MAX(id) + 4 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_CITY_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'NECROPOLIS_VILLAGE_BUILDINGS'),null),
            ((SELECT MAX(id) + 5 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_CITY_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'NECROPOLIS_TOWN_BUILDINGS'),null),
            ((SELECT MAX(id) + 6 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_CITY_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'NECROPOLIS_CITY_BUILDINGS'),null),
            ((SELECT MAX(id) + 7 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_CAPITOL'),(SELECT id FROM town_build_anim WHERE full_name = 'NECROPOLIS_VILLAGE_BUILDINGS'),null),
            ((SELECT MAX(id) + 8 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_CAPITOL'),(SELECT id FROM town_build_anim WHERE full_name = 'NECROPOLIS_TOWN_BUILDINGS'),null),
            ((SELECT MAX(id) + 9 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_CAPITOL'),(SELECT id FROM town_build_anim WHERE full_name = 'NECROPOLIS_CITY_BUILDINGS'),null),
            ((SELECT MAX(id) + 10 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_CAPITOL'),(SELECT id FROM town_build_anim WHERE full_name = 'NECROPOLIS_CAPITOL_BUILDINGS'),null),
            ((SELECT MAX(id) + 11 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'CURSED_TEMPLE'),(SELECT id FROM town_build_anim WHERE full_name = 'UNEARTHED_GRAVES'),(SELECT id FROM town_building WHERE full_name = 'UNEARTHED_GRAVES')),
            ((SELECT MAX(id) + 12 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'UPGR_CURSED_TEMPLE'),(SELECT id FROM town_build_anim WHERE full_name = 'UPGR_UNEARTHED_GRAVES'),(SELECT id FROM town_building WHERE full_name = 'UNEARTHED_GRAVES'));

            insert into building_prereq (
            id, building_id, prereq_id
            ) values
            ((SELECT MAX(id) + 1 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_TOWN_HALL'),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_TAVERN')),
            ((SELECT MAX(id) + 2 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_RESOURCE_SILO'),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_MARKETPLACE')),
            ((SELECT MAX(id) + 3 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_MARKETPLACE')),
            ((SELECT MAX(id) + 4 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_MAGE_GUILD_1')),
            ((SELECT MAX(id) + 5 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_BLACKSMITH')),
            ((SELECT MAX(id) + 6 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_CAPITOL'),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_CASTLE')),
            ((SELECT MAX(id) + 7 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'NECROMANCY_AMPLIFIER'),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_MAGE_GUILD_1')),
            ((SELECT MAX(id) + 8 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'COVER_OF_DARKNESS'),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_FORT')),
            ((SELECT MAX(id) + 9 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'CURSED_TEMPLE'),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_FORT')),
            ((SELECT MAX(id) + 10 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'GRAVEYARD'),(SELECT id FROM town_building WHERE full_name = 'CURSED_TEMPLE')),
            ((SELECT MAX(id) + 11 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'TOMB_OF_SOULS'),(SELECT id FROM town_building WHERE full_name = 'CURSED_TEMPLE')),
            ((SELECT MAX(id) + 12 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'ESTATE'),(SELECT id FROM town_building WHERE full_name = 'GRAVEYARD')),
            ((SELECT MAX(id) + 13 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'UPGR_ESTATE'),(SELECT id FROM town_building WHERE full_name = 'NECROMANCY_AMPLIFIER')),
            ((SELECT MAX(id) + 14 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'MAUSOLEUM'),(SELECT id FROM town_building WHERE full_name = 'GRAVEYARD')),
            ((SELECT MAX(id) + 15 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'MAUSOLEUM'),(SELECT id FROM town_building WHERE full_name = 'NECROPOLIS_MAGE_GUILD_1')),
            ((SELECT MAX(id) + 16 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'HALL_OF_DARKNESS'),(SELECT id FROM town_building WHERE full_name = 'ESTATE')),
            ((SELECT MAX(id) + 17 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'HALL_OF_DARKNESS'),(SELECT id FROM town_building WHERE full_name = 'MAUSOLEUM')),
            ((SELECT MAX(id) + 18 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'DRAGON_VAULT'),(SELECT id FROM town_building WHERE full_name = 'HALL_OF_DARKNESS')),
            ((SELECT MAX(id) + 19 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'SKELETON_TRANSFORMER'),(SELECT id FROM town_building WHERE full_name = 'CURSED_TEMPLE')),
            ((SELECT MAX(id) + 20 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'UNEARTHED_GRAVES'),(SELECT id FROM town_building WHERE full_name = 'SKELETON_TRANSFORMER'));

            -- DUNGEON
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'DUNGEON_VILLAGE_HALL',10,0,0,178,64,1,0,234,0),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'GUARDIAN_OF_EARTH',26,0,0,238,186,1,562,24,1),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'DUNGEON_FORT',7,0,1,198,164,1,363,87,1),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'DUNGEON_TAVERN',5,0,2,74,66,1,211,297,1),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'DUNGEON_BLACKSMITH',16,0,3,88,94,1,544,248,1),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'DUNGEON_MARKETPLACE',14,1,0,190,56,1,590,318,1),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'DUNGEON_MAGE_GUILD_1',0,1,1,88,80,1,164,119,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'DUNGEON_VILLAGE_HALL',(SELECT id FROM castle WHERE full_name='DUNGEON'),null,(SELECT id from town_build_anim where full_name = 'DUNGEON_VILLAGE_HALL'),0,0,0,0,0,0,0),
            ((SELECT MAX(id) + 2 FROM town_building),'GUARDIAN_OF_EARTH',(SELECT id FROM castle WHERE full_name='DUNGEON'),null,(SELECT id from town_build_anim where full_name = 'GUARDIAN_OF_EARTH'),0,0,0,0,0,0,0),
            ((SELECT MAX(id) + 3 FROM town_building),'DUNGEON_FORT',(SELECT id FROM castle WHERE full_name='DUNGEON'),null,(SELECT id from town_build_anim where full_name = 'DUNGEON_FORT'),20,0,20,0,0,0,5000),
            ((SELECT MAX(id) + 4 FROM town_building),'DUNGEON_TAVERN',(SELECT id FROM castle WHERE full_name='DUNGEON'),null,(SELECT id from town_build_anim where full_name = 'DUNGEON_TAVERN'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 5 FROM town_building),'DUNGEON_BLACKSMITH',(SELECT id FROM castle WHERE full_name='DUNGEON'),null,(SELECT id from town_build_anim where full_name = 'DUNGEON_BLACKSMITH'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 6 FROM town_building),'DUNGEON_MARKETPLACE',(SELECT id FROM castle WHERE full_name='DUNGEON'),null,(SELECT id from town_build_anim where full_name = 'DUNGEON_MARKETPLACE'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 7 FROM town_building),'DUNGEON_MAGE_GUILD_1',(SELECT id FROM castle WHERE full_name='DUNGEON'),null,(SELECT id from town_build_anim where full_name = 'DUNGEON_MAGE_GUILD_1'),5,0,5,0,0,0,2000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'DUNGEON_TOWN_HALL',11,0,0,178,74,1,0,223,0),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'DUNGEON_RESOURCE_SILO',15,1,0,70,34,1,624,335,2),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'DUNGEON_CITADEL',8,0,1,198,244,1,363,87,1),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'DUNGEON_MAGE_GUILD_2',1,1,1,88,102,1,164,97,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'DUNGEON_TOWN_HALL',(SELECT id FROM castle WHERE full_name = 'DUNGEON'),(SELECT id FROM town_building WHERE full_name='DUNGEON_VILLAGE_HALL'),(SELECT id from town_build_anim where full_name = 'DUNGEON_TOWN_HALL'),0,0,0,0,0,0,2500),
            ((SELECT MAX(id) + 2 FROM town_building),'DUNGEON_RESOURCE_SILO',(SELECT id FROM castle WHERE full_name = 'DUNGEON'),null,(SELECT id from town_build_anim where full_name = 'DUNGEON_RESOURCE_SILO'),0,0,5,0,0,0,5000),
            ((SELECT MAX(id) + 3 FROM town_building),'DUNGEON_CITADEL',(SELECT id FROM castle WHERE full_name = 'DUNGEON'),(SELECT id FROM town_building WHERE full_name='DUNGEON_FORT'),(SELECT id from town_build_anim where full_name = 'DUNGEON_CITADEL'),0,0,5,0,0,0,2500),
            ((SELECT MAX(id) + 4 FROM town_building),'DUNGEON_MAGE_GUILD_2',(SELECT id FROM castle WHERE full_name = 'DUNGEON'),(SELECT id FROM town_building WHERE full_name='DUNGEON_MAGE_GUILD_1'),(SELECT id from town_build_anim where full_name = 'DUNGEON_MAGE_GUILD_2'),5,4,5,4,4,4,5000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'DUNGEON_CITY_HALL',12,0,0,178,74,1,0,223,0),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'DUNGEON_CASTLE',9,0,1,198,244,1,363,87,1),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'DUNGEON_MAGE_GUILD_3',2,1,1,88,122,1,164,77,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'DUNGEON_CITY_HALL',(SELECT id FROM castle WHERE full_name = 'DUNGEON'),(SELECT id FROM town_building WHERE full_name='DUNGEON_TOWN_HALL'),(SELECT id from town_build_anim where full_name = 'DUNGEON_CITY_HALL'),0,0,0,0,0,0,5000),
            ((SELECT MAX(id) + 2 FROM town_building),'DUNGEON_CASTLE',(SELECT id FROM castle WHERE full_name = 'DUNGEON'),(SELECT id FROM town_building WHERE full_name='DUNGEON_CITADEL'),(SELECT id from town_build_anim where full_name = 'DUNGEON_CASTLE'),10,0,10,0,0,0,5000),
            ((SELECT MAX(id) + 3 FROM town_building),'DUNGEON_MAGE_GUILD_3',(SELECT id FROM castle WHERE full_name = 'DUNGEON'),(SELECT id FROM town_building WHERE full_name='DUNGEON_MAGE_GUILD_2'),(SELECT id from town_build_anim where full_name = 'DUNGEON_MAGE_GUILD_3'),5,6,5,6,6,6,1000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'DUNGEON_CAPITOL',13,0,0,178,94,1,0,203,0),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'DUNGEON_MAGE_GUILD_4',3,1,1,88,138,1,164,61,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'DUNGEON_CAPITOL',(SELECT id FROM castle WHERE full_name = 'DUNGEON'),(SELECT id FROM town_building WHERE full_name='DUNGEON_CITY_HALL'),(SELECT id from town_build_anim where full_name = 'DUNGEON_CAPITOL'),0,0,0,0,0,0,10000),
            ((SELECT MAX(id) + 2 FROM town_building),'DUNGEON_MAGE_GUILD_4',(SELECT id FROM castle WHERE full_name = 'DUNGEON'),(SELECT id FROM town_building WHERE full_name='DUNGEON_MAGE_GUILD_3'),(SELECT id from town_build_anim where full_name = 'DUNGEON_MAGE_GUILD_4'),5,8,5,8,8,8,1000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'DUNGEON_MAGE_GUILD_5',4,1,1,88,184,1,164,15,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'DUNGEON_MAGE_GUILD_5',(SELECT id FROM castle WHERE full_name = 'DUNGEON'),(SELECT id FROM town_building WHERE full_name='DUNGEON_MAGE_GUILD_4'),(SELECT id from town_build_anim where full_name = 'DUNGEON_MAGE_GUILD_5'),5,10,5,10,10,10,1000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'WARREN',30,3,0,148,48,8,0,326,3),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'HARPY_LOFT',31,3,1,80,88,1,0,26,0),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'PILLAR_OF_EYES',32,3,2,102,66,10,118,308,2),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'CHAPEL_OF_STILLED_VOICES',33,3,3,104,84,1,300,29,0),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'LABYRINTH',34,4,0,96,60,1,551,186,0),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'MANTICORE_LAIR',35,4,1,158,88,1,270,253,0),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'DRAGON_CAVE',36,4,2,250,90,10,550,0,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'WARREN',(SELECT id FROM castle WHERE full_name='DUNGEON'),null,(SELECT id from town_build_anim where full_name = 'WARREN'),10,0,0,0,0,0,400),
            ((SELECT MAX(id) + 2 FROM town_building),'HARPY_LOFT',(SELECT id FROM castle WHERE full_name='DUNGEON'),null,(SELECT id from town_build_anim where full_name = 'HARPY_LOFT'),0,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'PILLAR_OF_EYES',(SELECT id FROM castle WHERE full_name='DUNGEON'),null,(SELECT id from town_build_anim where full_name = 'PILLAR_OF_EYES'),1,1,1,1,1,1,1000),
            ((SELECT MAX(id) + 4 FROM town_building),'CHAPEL_OF_STILLED_VOICES',(SELECT id FROM castle WHERE full_name='DUNGEON'),null,(SELECT id from town_build_anim where full_name = 'CHAPEL_OF_STILLED_VOICES'),5,0,10,0,0,0,2000),
            ((SELECT MAX(id) + 5 FROM town_building),'LABYRINTH',(SELECT id FROM castle WHERE full_name='DUNGEON'),null,(SELECT id from town_build_anim where full_name = 'LABYRINTH'),0,0,10,0,0,10,4000),
            ((SELECT MAX(id) + 6 FROM town_building),'MANTICORE_LAIR',(SELECT id FROM castle WHERE full_name='DUNGEON'),null,(SELECT id from town_build_anim where full_name = 'MANTICORE_LAIR'),5,5,5,5,0,0,5000),
            ((SELECT MAX(id) + 7 FROM town_building),'DRAGON_CAVE',(SELECT id FROM castle WHERE full_name='DUNGEON'),null,(SELECT id from town_build_anim where full_name = 'DRAGON_CAVE'),15,0,15,20,0,0,15000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'UPGR_WARREN',37,3,0,148,74,8,0,300,3),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'UPGR_HARPY_LOFT',38,3,1,80,88,1,0,26,0),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'UPGR_PILLAR_OF_EYES',39,3,2,102,118,10,118,256,2),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'UPGR_CHAPEL_OF_STILLED_VOICES',40,3,3,104,84,1,300,29,0),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'UPGR_LABYRINTH',41,4,0,128,74,1,519,172,0),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'UPGR_MANTICORE_LAIR',42,4,1,158,88,1,270,253,0),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'UPGR_DRAGON_CAVE',43,4,2,250,90,10,550,0,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'UPGR_WARREN',(SELECT id FROM castle WHERE full_name='DUNGEON'),(SELECT id from town_building WHERE full_name='WARREN'),(SELECT id from town_build_anim where full_name = 'UPGR_WARREN'),5,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 2 FROM town_building),'UPGR_HARPY_LOFT',(SELECT id FROM castle WHERE full_name='DUNGEON'),(SELECT id from town_building WHERE full_name='HARPY_LOFT'),(SELECT id from town_build_anim where full_name = 'UPGR_HARPY_LOFT'),0,0,0,2,2,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'UPGR_PILLAR_OF_EYES',(SELECT id FROM castle WHERE full_name='DUNGEON'),(SELECT id from town_building WHERE full_name='PILLAR_OF_EYES'),(SELECT id from town_build_anim where full_name = 'UPGR_PILLAR_OF_EYES'),1,1,1,1,1,1,1000),
            ((SELECT MAX(id) + 4 FROM town_building),'UPGR_CHAPEL_OF_STILLED_VOICES',(SELECT id FROM castle WHERE full_name='DUNGEON'),(SELECT id from town_building WHERE full_name='CHAPEL_OF_STILLED_VOICES'),(SELECT id from town_build_anim where full_name = 'UPGR_CHAPEL_OF_STILLED_VOICES'),5,0,0,0,0,0,1500),
            ((SELECT MAX(id) + 5 FROM town_building),'UPGR_LABYRINTH',(SELECT id FROM castle WHERE full_name='DUNGEON'),(SELECT id from town_building WHERE full_name='LABYRINTH'),(SELECT id from town_build_anim where full_name = 'UPGR_LABYRINTH'),0,0,5,0,0,5,3000),
            ((SELECT MAX(id) + 6 FROM town_building),'UPGR_MANTICORE_LAIR',(SELECT id FROM castle WHERE full_name='DUNGEON'),(SELECT id from town_building WHERE full_name='MANTICORE_LAIR'),(SELECT id from town_build_anim where full_name = 'UPGR_MANTICORE_LAIR'),5,5,5,5,0,0,3000),
            ((SELECT MAX(id) + 7 FROM town_building),'UPGR_DRAGON_CAVE',(SELECT id FROM castle WHERE full_name='DUNGEON'),(SELECT id from town_building WHERE full_name='DRAGON_CAVE'),(SELECT id from town_build_anim where full_name = 'UPGR_DRAGON_CAVE'),15,0,15,20,0,0,15000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'DUNGEON_ARTIFACT_MERCHANTS',17,2,0,54,80,1,746,294,2),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'EMPTY_MUSHROOM_RINGS',18,2,2,0,0,0,0,0,3),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'MUSHROOM_RINGS',18,2,2,148,48,8,0,326,3),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'UPGR_MUSHROOM_RINGS',19,2,2,148,74,8,0,300,3),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'MANA_VORTEX',21,1,2,144,34,20,131,26,1),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'PORTAL_OF_SUMMONING',22,1,3,100,100,10,687,177,0),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'BATTLE_SCHOLAR_ACADEMY',23,2,1,134,76,1,313,298,1);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'DUNGEON_ARTIFACT_MERCHANTS',(SELECT id FROM castle WHERE full_name='DUNGEON'),null,(SELECT id from town_build_anim where full_name = 'DUNGEON_ARTIFACT_MERCHANTS'),0,0,0,0,0,0,10000),
            ((SELECT MAX(id) + 2 FROM town_building),'MUSHROOM_RINGS',(SELECT id FROM castle WHERE full_name='DUNGEON'),null,(SELECT id from town_build_anim where full_name = 'EMPTY_MUSHROOM_RINGS'),0,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'MANA_VORTEX',(SELECT id FROM castle WHERE full_name='DUNGEON'),null,(SELECT id from town_build_anim where full_name = 'MANA_VORTEX'),0,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 4 FROM town_building),'PORTAL_OF_SUMMONING',(SELECT id FROM castle WHERE full_name='DUNGEON'),null,(SELECT id from town_build_anim where full_name = 'PORTAL_OF_SUMMONING'),0,0,5,0,0,0,2500),
            ((SELECT MAX(id) + 5 FROM town_building),'BATTLE_SCHOLAR_ACADEMY',(SELECT id FROM castle WHERE full_name='DUNGEON'),null,(SELECT id from town_build_anim where full_name = 'BATTLE_SCHOLAR_ACADEMY'),5,0,5,0,0,0,1000);

            insert into additional_build_anim (
            id, building_id, animation_id, existing_building_id
            ) values
            ((SELECT MAX(id) + 1 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'WARREN'),(SELECT id FROM town_build_anim WHERE full_name = 'MUSHROOM_RINGS'),(SELECT id FROM town_building WHERE full_name = 'MUSHROOM_RINGS')),
            ((SELECT MAX(id) + 2 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'UPGR_WARREN'),(SELECT id FROM town_build_anim WHERE full_name = 'UPGR_MUSHROOM_RINGS'),(SELECT id FROM town_building WHERE full_name = 'MUSHROOM_RINGS'));

            insert into building_prereq (
            id, building_id, prereq_id
            ) values
            ((SELECT MAX(id) + 1 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'DUNGEON_TOWN_HALL'),(SELECT id FROM town_building WHERE full_name = 'DUNGEON_TAVERN')),
            ((SELECT MAX(id) + 2 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'DUNGEON_RESOURCE_SILO'),(SELECT id FROM town_building WHERE full_name = 'DUNGEON_MARKETPLACE')),
            ((SELECT MAX(id) + 3 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'DUNGEON_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'DUNGEON_MARKETPLACE')),
            ((SELECT MAX(id) + 4 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'DUNGEON_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'DUNGEON_MAGE_GUILD_1')),
            ((SELECT MAX(id) + 5 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'DUNGEON_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'DUNGEON_BLACKSMITH')),
            ((SELECT MAX(id) + 6 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'DUNGEON_CAPITOL'),(SELECT id FROM town_building WHERE full_name = 'DUNGEON_CASTLE')),
            ((SELECT MAX(id) + 7 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'DUNGEON_ARTIFACT_MERCHANTS'),(SELECT id FROM town_building WHERE full_name = 'DUNGEON_MARKETPLACE')),
            ((SELECT MAX(id) + 8 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'MANA_VORTEX'),(SELECT id FROM town_building WHERE full_name = 'DUNGEON_MAGE_GUILD_1')),
            ((SELECT MAX(id) + 9 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'WARREN'),(SELECT id FROM town_building WHERE full_name = 'DUNGEON_FORT')),
            ((SELECT MAX(id) + 10 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'MUSHROOM_RINGS'),(SELECT id FROM town_building WHERE full_name = 'WARREN')),
            ((SELECT MAX(id) + 11 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'HARPY_LOFT'),(SELECT id FROM town_building WHERE full_name = 'WARREN')),
            ((SELECT MAX(id) + 12 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'PILLAR_OF_EYES'),(SELECT id FROM town_building WHERE full_name = 'WARREN')),
            ((SELECT MAX(id) + 13 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'CHAPEL_OF_STILLED_VOICES'),(SELECT id FROM town_building WHERE full_name = 'HARPY_LOFT')),
            ((SELECT MAX(id) + 14 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'CHAPEL_OF_STILLED_VOICES'),(SELECT id FROM town_building WHERE full_name = 'PILLAR_OF_EYES')),
            ((SELECT MAX(id) + 15 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'LABYRINTH'),(SELECT id FROM town_building WHERE full_name = 'CHAPEL_OF_STILLED_VOICES')),
            ((SELECT MAX(id) + 16 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'MANTICORE_LAIR'),(SELECT id FROM town_building WHERE full_name = 'CHAPEL_OF_STILLED_VOICES')),
            ((SELECT MAX(id) + 17 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'DRAGON_CAVE'),(SELECT id FROM town_building WHERE full_name = 'LABYRINTH')),
            ((SELECT MAX(id) + 18 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'DRAGON_CAVE'),(SELECT id FROM town_building WHERE full_name = 'MANTICORE_LAIR')),
            ((SELECT MAX(id) + 19 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'DRAGON_CAVE'),(SELECT id FROM town_building WHERE full_name = 'DUNGEON_MAGE_GUILD_2')),
            ((SELECT MAX(id) + 20 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'UPGR_DRAGON_CAVE'),(SELECT id FROM town_building WHERE full_name = 'DUNGEON_MAGE_GUILD_3'));

            -- STRONGHOLD
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'STRONGHOLD_VILLAGE_HALL',10,0,0,248,108,20,0,259,2),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'WARLORDS_MONUMENT',26,0,0,98,262,10,321,105,4),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'STRONGHOLD_FORT',7,0,1,208,76,1,402,148,1),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'STRONGHOLD_TAVERN',5,0,2,156,92,1,170,280,4),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'STRONGHOLD_BLACKSMITH',16,0,3,124,82,1,660,286,3),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'STRONGHOLD_MARKETPLACE',14,1,0,196,66,1,397,308,2),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'STRONGHOLD_MAGE_GUILD_1',0,1,1,82,78,1,473,67,0),
            ((SELECT MAX(id) + 8 FROM town_build_anim),'STRONGHOLD_WATERFALL',0,0,0,34,108,20,23,20,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'STRONGHOLD_VILLAGE_HALL',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),null,(SELECT id from town_build_anim where full_name = 'STRONGHOLD_VILLAGE_HALL'),0,0,0,0,0,0,0),
            ((SELECT MAX(id) + 2 FROM town_building),'WARLORDS_MONUMENT',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),null,(SELECT id from town_build_anim where full_name = 'WARLORDS_MONUMENT'),26,0,0,0,0,0,0),
            ((SELECT MAX(id) + 3 FROM town_building),'STRONGHOLD_FORT',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),null,(SELECT id from town_build_anim where full_name = 'STRONGHOLD_FORT'),20,0,20,0,0,0,5000),
            ((SELECT MAX(id) + 4 FROM town_building),'STRONGHOLD_TAVERN',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),null,(SELECT id from town_build_anim where full_name = 'STRONGHOLD_TAVERN'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 5 FROM town_building),'STRONGHOLD_BLACKSMITH',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),null,(SELECT id from town_build_anim where full_name = 'STRONGHOLD_BLACKSMITH'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 6 FROM town_building),'STRONGHOLD_MARKETPLACE',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),null,(SELECT id from town_build_anim where full_name = 'STRONGHOLD_MARKETPLACE'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 7 FROM town_building),'STRONGHOLD_MAGE_GUILD_1',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),null,(SELECT id from town_build_anim where full_name = 'STRONGHOLD_MAGE_GUILD_1'),5,0,5,0,0,0,2000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'STRONGHOLD_TOWN_HALL',11,0,0,248,142,20,0,225,2),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'STRONGHOLD_RESOURCE_SILO',15,1,0,31,82,1,458,248,4),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'STRONGHOLD_CITADEL',8,0,1,208,110,1,402,114,1),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'STRONGHOLD_MAGE_GUILD_2',1,1,1,81,108,1,474,37,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'STRONGHOLD_TOWN_HALL',(SELECT id FROM castle WHERE full_name = 'STRONGHOLD'),(SELECT id FROM town_building WHERE full_name='STRONGHOLD_VILLAGE_HALL'),(SELECT id from town_build_anim where full_name = 'STRONGHOLD_TOWN_HALL'),0,0,0,0,0,0,2500),
            ((SELECT MAX(id) + 2 FROM town_building),'STRONGHOLD_RESOURCE_SILO',(SELECT id FROM castle WHERE full_name = 'STRONGHOLD'),null,(SELECT id from town_build_anim where full_name = 'STRONGHOLD_RESOURCE_SILO'),0,0,5,0,0,0,5000),
            ((SELECT MAX(id) + 3 FROM town_building),'STRONGHOLD_CITADEL',(SELECT id FROM castle WHERE full_name = 'STRONGHOLD'),(SELECT id FROM town_building WHERE full_name='STRONGHOLD_FORT'),(SELECT id from town_build_anim where full_name = 'STRONGHOLD_CITADEL'),0,0,5,0,0,0,2500),
            ((SELECT MAX(id) + 4 FROM town_building),'STRONGHOLD_MAGE_GUILD_2',(SELECT id FROM castle WHERE full_name = 'STRONGHOLD'),(SELECT id FROM town_building WHERE full_name='STRONGHOLD_MAGE_GUILD_1'),(SELECT id from town_build_anim where full_name = 'STRONGHOLD_MAGE_GUILD_2'),5,4,5,4,4,4,5000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'STRONGHOLD_CITY_HALL',12,0,0,248,166,20,0,201,2),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'STRONGHOLD_CASTLE',9,0,1,210,110,1,402,114,1),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'STRONGHOLD_MAGE_GUILD_3',2,1,1,81,144,1,473,1,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'STRONGHOLD_CITY_HALL',(SELECT id FROM castle WHERE full_name = 'STRONGHOLD'),(SELECT id FROM town_building WHERE full_name='STRONGHOLD_TOWN_HALL'),(SELECT id from town_build_anim where full_name = 'STRONGHOLD_CITY_HALL'),0,0,0,0,0,0,5000),
            ((SELECT MAX(id) + 2 FROM town_building),'STRONGHOLD_CASTLE',(SELECT id FROM castle WHERE full_name = 'STRONGHOLD'),(SELECT id FROM town_building WHERE full_name='STRONGHOLD_CITADEL'),(SELECT id from town_build_anim where full_name = 'STRONGHOLD_CASTLE'),10,0,10,0,0,0,5000),
            ((SELECT MAX(id) + 3 FROM town_building),'STRONGHOLD_MAGE_GUILD_3',(SELECT id FROM castle WHERE full_name = 'STRONGHOLD'),(SELECT id FROM town_building WHERE full_name='STRONGHOLD_MAGE_GUILD_2'),(SELECT id from town_build_anim where full_name = 'STRONGHOLD_MAGE_GUILD_3'),5,6,5,6,6,6,1000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'STRONGHOLD_CAPITOL',13,0,0,248,220,20,0,148,2);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'STRONGHOLD_CAPITOL',(SELECT id FROM castle WHERE full_name = 'STRONGHOLD'),(SELECT id FROM town_building WHERE full_name='STRONGHOLD_CITY_HALL'),(SELECT id from town_build_anim where full_name = 'STRONGHOLD_CAPITOL'),0,0,0,0,0,0,10000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'GOBLIN_BARRACKS',30,3,0,132,58,1,373,239,1),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'WOLF_PEN',31,3,1,156,64,1,266,246,2),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'ORC_TOWER',32,3,2,92,86,1,566,232,2),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'OGRE_FORT',33,3,3,112,70,1,197,204,1),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'CLIFF_NEST',34,4,0,144,174,1,137,30,0),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'CYCLOPS_CAVE',35,4,1,178,112,1,622,160,1),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'BEHEMOTH_LAIR',36,4,2,176,178,10,604,0,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'GOBLIN_BARRACKS',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),null,(SELECT id from town_build_anim where full_name = 'GOBLIN_BARRACKS'),5,0,5,0,0,0,200),
            ((SELECT MAX(id) + 2 FROM town_building),'WOLF_PEN',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),null,(SELECT id from town_build_anim where full_name = 'WOLF_PEN'),10,0,5,0,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'ORC_TOWER',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),null,(SELECT id from town_build_anim where full_name = 'ORC_TOWER'),5,0,5,0,0,0,1000),
            ((SELECT MAX(id) + 4 FROM town_building),'OGRE_FORT',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),null,(SELECT id from town_build_anim where full_name = 'OGRE_FORT'),20,0,0,0,0,0,2000),
            ((SELECT MAX(id) + 5 FROM town_building),'CLIFF_NEST',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),null,(SELECT id from town_build_anim where full_name = 'CLIFF_NEST'),0,0,10,0,0,0,2500),
            ((SELECT MAX(id) + 6 FROM town_building),'CYCLOPS_CAVE',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),null,(SELECT id from town_build_anim where full_name = 'CYCLOPS_CAVE'),0,0,20,0,20,0,3500),
            ((SELECT MAX(id) + 7 FROM town_building),'BEHEMOTH_LAIR',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),null,(SELECT id from town_build_anim where full_name = 'BEHEMOTH_LAIR'),10,0,10,0,10,0,10000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'UPGR_GOBLIN_BARRACKS',37,3,0,132,78,1,373,220,1),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'UPGR_WOLF_PEN',38,3,1,156,84,1,266,225,2),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'UPGR_ORC_TOWER',39,3,2,92,160,1,566,158,2),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'UPGR_OGRE_FORT',40,3,3,112,138,1,197,137,1),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'UPGR_CLIFF_NEST',41,4,0,152,190,1,129,15,0),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'UPGR_CYCLOPS_CAVE',42,4,1,184,180,10,616,93,1),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'UPGR_BEHEMOTH_LAIR',43,4,2,176,178,10,604,0,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'UPGR_GOBLIN_BARRACKS',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),(SELECT id from town_building WHERE full_name='GOBLIN_BARRACKS'),(SELECT id from town_build_anim where full_name = 'UPGR_GOBLIN_BARRACKS'),5,0,5,0,0,0,1000),
            ((SELECT MAX(id) + 2 FROM town_building),'UPGR_WOLF_PEN',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),(SELECT id from town_building WHERE full_name='WOLF_PEN'),(SELECT id from town_build_anim where full_name = 'UPGR_WOLF_PEN'),5,0,5,0,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'UPGR_ORC_TOWER',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),(SELECT id from town_building WHERE full_name='ORC_TOWER'),(SELECT id from town_build_anim where full_name = 'UPGR_ORC_TOWER'),2,0,2,0,0,0,1000),
            ((SELECT MAX(id) + 4 FROM town_building),'UPGR_OGRE_FORT',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),(SELECT id from town_building WHERE full_name='OGRE_FORT'),(SELECT id from town_build_anim where full_name = 'UPGR_OGRE_FORT'),5,0,5,0,0,5,2000),
            ((SELECT MAX(id) + 5 FROM town_building),'UPGR_CLIFF_NEST',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),(SELECT id from town_building WHERE full_name='CLIFF_NEST'),(SELECT id from town_build_anim where full_name = 'UPGR_CLIFF_NEST'),5,0,5,0,0,0,2000),
            ((SELECT MAX(id) + 6 FROM town_building),'UPGR_CYCLOPS_CAVE',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),(SELECT id from town_building WHERE full_name='CYCLOPS_CAVE'),(SELECT id from town_build_anim where full_name = 'UPGR_CYCLOPS_CAVE'),5,0,5,0,0,0,3000),
            ((SELECT MAX(id) + 7 FROM town_building),'UPGR_BEHEMOTH_LAIR',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),(SELECT id from town_building WHERE full_name='BEHEMOTH_LAIR'),(SELECT id from town_build_anim where full_name = 'UPGR_BEHEMOTH_LAIR'),10,0,10,0,20,0,15000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'ESCAPE_TUNNEL',17,1,3,86,42,20,550,229,1),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'EMPTY_MESS_HALL',18,2,2,0,0,0,0,0,1),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'MESS_HALL',18,2,2,132,58,1,373,239,1),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'UPGR_MESS_HALL',19,2,2,132,78,1,373,220,1),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'FREELANCERS_GUILD',21,2,0,73,62,1,473,282,3),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'BALLISTA_YARD',22,2,1,63,84,1,617,286,4),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'HALL_OF_VALHALLA',23,1,2,80,182,1,313,13,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'ESCAPE_TUNNEL',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),null,(SELECT id from town_build_anim where full_name = 'ESCAPE_TUNNEL'),5,0,5,0,0,0,2000),
            ((SELECT MAX(id) + 2 FROM town_building),'MESS_HALL',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),null,(SELECT id from town_build_anim where full_name = 'EMPTY_MESS_HALL'),0,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'FREELANCERS_GUILD',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),null,(SELECT id from town_build_anim where full_name = 'FREELANCERS_GUILD'),0,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 4 FROM town_building),'BALLISTA_YARD',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),null,(SELECT id from town_build_anim where full_name = 'BALLISTA_YARD'),5,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 5 FROM town_building),'HALL_OF_VALHALLA',(SELECT id FROM castle WHERE full_name='STRONGHOLD'),null,(SELECT id from town_build_anim where full_name = 'HALL_OF_VALHALLA'),0,0,0,0,0,0,1000);

            insert into additional_build_anim (
            id, building_id, animation_id, existing_building_id
            ) values
            ((SELECT MAX(id) + 1 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'GOBLIN_BARRACKS'),(SELECT id FROM town_build_anim WHERE full_name = 'MESS_HALL'),(SELECT id FROM town_building WHERE full_name = 'MESS_HALL')),
            ((SELECT MAX(id) + 2 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'UPGR_GOBLIN_BARRACKS'),(SELECT id FROM town_build_anim WHERE full_name = 'UPGR_MESS_HALL'),(SELECT id FROM town_building WHERE full_name = 'MESS_HALL')),
            ((SELECT MAX(id) + 3 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_VILLAGE_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'STRONGHOLD_WATERFALL'),null),
            ((SELECT MAX(id) + 4 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_TOWN_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'STRONGHOLD_WATERFALL'),null),
            ((SELECT MAX(id) + 5 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_CITY_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'STRONGHOLD_WATERFALL'),null),
            ((SELECT MAX(id) + 6 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_CAPITOL'),(SELECT id FROM town_build_anim WHERE full_name = 'STRONGHOLD_WATERFALL'),null);

            insert into building_prereq (
            id, building_id, prereq_id
            ) values
            ((SELECT MAX(id) + 1 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_TOWN_HALL'),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_TAVERN')),
            ((SELECT MAX(id) + 2 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_RESOURCE_SILO'),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_MARKETPLACE')),
            ((SELECT MAX(id) + 3 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_MARKETPLACE')),
            ((SELECT MAX(id) + 4 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_MAGE_GUILD_1')),
            ((SELECT MAX(id) + 5 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_BLACKSMITH')),
            ((SELECT MAX(id) + 6 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_CAPITOL'),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_CASTLE')),
            ((SELECT MAX(id) + 7 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'FREELANCERS_GUILD'),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_MARKETPLACE')),
            ((SELECT MAX(id) + 8 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'BALLISTA_YARD'),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_BLACKSMITH')),
            ((SELECT MAX(id) + 9 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'ESCAPE_TUNNEL'),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_FORT')),
            ((SELECT MAX(id) + 10 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'HALL_OF_VALHALLA'),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_FORT')),
            ((SELECT MAX(id) + 11 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'GOBLIN_BARRACKS'),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_FORT')),
            ((SELECT MAX(id) + 12 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'WOLF_PEN'),(SELECT id FROM town_building WHERE full_name = 'GOBLIN_BARRACKS')),
            ((SELECT MAX(id) + 13 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'ORC_TOWER'),(SELECT id FROM town_building WHERE full_name = 'GOBLIN_BARRACKS')),
            ((SELECT MAX(id) + 14 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'UPGR_WOLF_PEN'),(SELECT id FROM town_building WHERE full_name = 'UPGR_GOBLIN_BARRACKS')),
            ((SELECT MAX(id) + 15 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'UPGR_ORC_TOWER'),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_BLACKSMITH')),
            ((SELECT MAX(id) + 16 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'OGRE_FORT'),(SELECT id FROM town_building WHERE full_name = 'ORC_TOWER')),
            ((SELECT MAX(id) + 17 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'UPGR_OGRE_FORT'),(SELECT id FROM town_building WHERE full_name = 'STRONGHOLD_MAGE_GUILD_1')),
            ((SELECT MAX(id) + 18 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'CLIFF_NEST'),(SELECT id FROM town_building WHERE full_name = 'WOLF_PEN')),
            ((SELECT MAX(id) + 19 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'BEHEMOTH_LAIR'),(SELECT id FROM town_building WHERE full_name = 'CLIFF_NEST')),
            ((SELECT MAX(id) + 20 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'CYCLOPS_CAVE'),(SELECT id FROM town_building WHERE full_name = 'OGRE_FORT')),
            ((SELECT MAX(id) + 21 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'MESS_HALL'),(SELECT id FROM town_building WHERE full_name = 'GOBLIN_BARRACKS'));

            -- FORTRESS
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'FORTRESS_VILLAGE_HALL',10,0,0,252,87,1,166,128,4),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'CARNIVOROUS_PLANT',26,0,0,132,114,1,468,260,5),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'FORTRESS_FORT',7,0,1,369,169,1,368,118,1),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'FORTRESS_TAVERN',5,0,2,166,126,21,634,219,4),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'FORTRESS_BLACKSMITH',16,0,3,95,102,1,360,160,3),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'FORTRESS_MARKETPLACE',14,1,0,155,116,41,382,219,4),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'FORTRESS_MAGE_GUILD_1',0,1,1,136,102,1,0,200,1),
            ((SELECT MAX(id) + 8 FROM town_build_anim),'FORTRESS_SHIPYARD',6,1,2,175,80,1,197,294,2),
            ((SELECT MAX(id) + 9 FROM town_build_anim),'FORTRESS_LAKE',0,0,0,282,147,20,372,227,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'FORTRESS_VILLAGE_HALL',(SELECT id FROM castle WHERE full_name='FORTRESS'),null,(SELECT id from town_build_anim where full_name = 'FORTRESS_VILLAGE_HALL'),0,0,0,0,0,0,0),
            ((SELECT MAX(id) + 2 FROM town_building),'CARNIVOROUS_PLANT',(SELECT id FROM castle WHERE full_name='FORTRESS'),null,(SELECT id from town_build_anim where full_name = 'CARNIVOROUS_PLANT'),0,0,0,0,0,0,0),
            ((SELECT MAX(id) + 3 FROM town_building),'FORTRESS_FORT',(SELECT id FROM castle WHERE full_name='FORTRESS'),null,(SELECT id from town_build_anim where full_name = 'FORTRESS_FORT'),20,0,20,0,0,0,5000),
            ((SELECT MAX(id) + 4 FROM town_building),'FORTRESS_TAVERN',(SELECT id FROM castle WHERE full_name='FORTRESS'),null,(SELECT id from town_build_anim where full_name = 'FORTRESS_TAVERN'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 5 FROM town_building),'FORTRESS_BLACKSMITH',(SELECT id FROM castle WHERE full_name='FORTRESS'),null,(SELECT id from town_build_anim where full_name = 'FORTRESS_BLACKSMITH'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 6 FROM town_building),'FORTRESS_MARKETPLACE',(SELECT id FROM castle WHERE full_name='FORTRESS'),null,(SELECT id from town_build_anim where full_name = 'FORTRESS_MARKETPLACE'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 7 FROM town_building),'FORTRESS_MAGE_GUILD_1',(SELECT id FROM castle WHERE full_name='FORTRESS'),null,(SELECT id from town_build_anim where full_name = 'FORTRESS_MAGE_GUILD_1'),5,0,5,0,0,0,2000),
            ((SELECT MAX(id) + 8 FROM town_building),'FORTRESS_SHIPYARD',(SELECT id FROM castle WHERE full_name='FORTRESS'),null,(SELECT id from town_build_anim where full_name = 'FORTRESS_SHIPYARD'),20,0,0,0,0,0,2000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'FORTRESS_TOWN_HALL',11,0,0,252,118,1,166,97,4),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'FORTRESS_RESOURCE_SILO',15,1,0,58,48,7,448,210,3),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'FORTRESS_CITADEL',8,0,1,369,189,1,368,98,1),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'FORTRESS_MAGE_GUILD_2',1,1,1,136,125,1,0,177,1);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'FORTRESS_TOWN_HALL',(SELECT id FROM castle WHERE full_name = 'FORTRESS'),(SELECT id FROM town_building WHERE full_name='FORTRESS_VILLAGE_HALL'),(SELECT id from town_build_anim where full_name = 'FORTRESS_TOWN_HALL'),0,0,0,0,0,0,2500),
            ((SELECT MAX(id) + 2 FROM town_building),'FORTRESS_RESOURCE_SILO',(SELECT id FROM castle WHERE full_name = 'FORTRESS'),null,(SELECT id from town_build_anim where full_name = 'FORTRESS_RESOURCE_SILO'),0,0,5,0,0,0,5000),
            ((SELECT MAX(id) + 3 FROM town_building),'FORTRESS_CITADEL',(SELECT id FROM castle WHERE full_name = 'FORTRESS'),(SELECT id FROM town_building WHERE full_name='FORTRESS_FORT'),(SELECT id from town_build_anim where full_name = 'FORTRESS_CITADEL'),0,0,5,0,0,0,2500),
            ((SELECT MAX(id) + 4 FROM town_building),'FORTRESS_MAGE_GUILD_2',(SELECT id FROM castle WHERE full_name = 'FORTRESS'),(SELECT id FROM town_building WHERE full_name='FORTRESS_MAGE_GUILD_1'),(SELECT id from town_build_anim where full_name = 'FORTRESS_MAGE_GUILD_2'),5,4,5,4,4,4,5000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'FORTRESS_CITY_HALL',12,0,0,252,164,1,166,51,4),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'FORTRESS_CASTLE',9,0,1,369,232,21,368,55,1),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'FORTRESS_MAGE_GUILD_3',2,1,1,136,167,21,0,135,1);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'FORTRESS_CITY_HALL',(SELECT id FROM castle WHERE full_name = 'FORTRESS'),(SELECT id FROM town_building WHERE full_name='FORTRESS_TOWN_HALL'),(SELECT id from town_build_anim where full_name = 'FORTRESS_CITY_HALL'),0,0,0,0,0,0,5000),
            ((SELECT MAX(id) + 2 FROM town_building),'FORTRESS_CASTLE',(SELECT id FROM castle WHERE full_name = 'FORTRESS'),(SELECT id FROM town_building WHERE full_name='FORTRESS_CITADEL'),(SELECT id from town_build_anim where full_name = 'FORTRESS_CASTLE'),10,0,10,0,0,0,5000),
            ((SELECT MAX(id) + 3 FROM town_building),'FORTRESS_MAGE_GUILD_3',(SELECT id FROM castle WHERE full_name = 'FORTRESS'),(SELECT id FROM town_building WHERE full_name='FORTRESS_MAGE_GUILD_2'),(SELECT id from town_build_anim where full_name = 'FORTRESS_MAGE_GUILD_3'),5,6,5,6,6,6,1000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'FORTRESS_CAPITOL',13,0,0,252,213,1,166,2,4);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'FORTRESS_CAPITOL',(SELECT id FROM castle WHERE full_name = 'FORTRESS'),(SELECT id FROM town_building WHERE full_name='FORTRESS_CITY_HALL'),(SELECT id from town_build_anim where full_name = 'FORTRESS_CAPITOL'),0,0,0,0,0,0,10000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'GNOLL_HUT',30,3,0,144,100,1,641,168,3),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'LIZARD_DEN',31,3,1,177,98,1,141,178,1),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'SERPENT_FLY_HIVE',32,3,2,42,49,21,192,88,3),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'BASILISK_PIT',33,3,3,179,80,1,0,293,2),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'GORGON_LAIR',34,4,0,255,105,1,15,127,0),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'WYVERN_NEST',35,4,1,112,108,1,0,4,1),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'HYDRA_POND',36,4,2,188,83,21,612,291,5);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'GNOLL_HUT',(SELECT id FROM castle WHERE full_name='FORTRESS'),null,(SELECT id from town_build_anim where full_name = 'GNOLL_HUT'),10,0,0,0,0,0,400),
            ((SELECT MAX(id) + 2 FROM town_building),'LIZARD_DEN',(SELECT id FROM castle WHERE full_name='FORTRESS'),null,(SELECT id from town_build_anim where full_name = 'LIZARD_DEN'),5,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'SERPENT_FLY_HIVE',(SELECT id FROM castle WHERE full_name='FORTRESS'),null,(SELECT id from town_build_anim where full_name = 'SERPENT_FLY_HIVE'),5,2,0,2,0,0,1000),
            ((SELECT MAX(id) + 4 FROM town_building),'BASILISK_PIT',(SELECT id FROM castle WHERE full_name='FORTRESS'),null,(SELECT id from town_build_anim where full_name = 'BASILISK_PIT'),5,0,10,0,0,0,2000),
            ((SELECT MAX(id) + 5 FROM town_building),'GORGON_LAIR',(SELECT id FROM castle WHERE full_name='FORTRESS'),null,(SELECT id from town_build_anim where full_name = 'GORGON_LAIR'),10,5,10,5,0,0,2500),
            ((SELECT MAX(id) + 6 FROM town_building),'WYVERN_NEST',(SELECT id FROM castle WHERE full_name='FORTRESS'),null,(SELECT id from town_build_anim where full_name = 'WYVERN_NEST'),15,0,0,0,0,0,3500),
            ((SELECT MAX(id) + 7 FROM town_building),'HYDRA_POND',(SELECT id FROM castle WHERE full_name='FORTRESS'),null,(SELECT id from town_build_anim where full_name = 'HYDRA_POND'),10,0,10,10,0,0,10000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'UPGR_GNOLL_HUT',37,3,0,159,161,1,641,107,3),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'UPGR_LIZARD_DEN',38,3,1,208,125,1,125,163,1),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'UPGR_SERPENT_FLY_HIVE',39,3,2,87,134,21,159,19,3),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'UPGR_BASILISK_PIT',40,3,3,178,115,21,0,257,2),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'UPGR_GORGON_LAIR',41,4,0,255,163,1,15,69,0),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'UPGR_WYVERN_NEST',42,4,1,163,118,1,0,4,1),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'UPGR_HYDRA_POND',43,4,2,213,111,20,587,263,5);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'UPGR_GNOLL_HUT',(SELECT id FROM castle WHERE full_name='FORTRESS'),(SELECT id from town_building WHERE full_name='GNOLL_HUT'),(SELECT id from town_build_anim where full_name = 'UPGR_GNOLL_HUT'),10,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 2 FROM town_building),'UPGR_LIZARD_DEN',(SELECT id FROM castle WHERE full_name='FORTRESS'),(SELECT id from town_building WHERE full_name='LIZARD_DEN'),(SELECT id from town_build_anim where full_name = 'UPGR_LIZARD_DEN'),5,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'UPGR_SERPENT_FLY_HIVE',(SELECT id FROM castle WHERE full_name='FORTRESS'),(SELECT id from town_building WHERE full_name='SERPENT_FLY_HIVE'),(SELECT id from town_build_anim where full_name = 'UPGR_SERPENT_FLY_HIVE'),0,2,0,2,0,0,1000),
            ((SELECT MAX(id) + 4 FROM town_building),'UPGR_BASILISK_PIT',(SELECT id FROM castle WHERE full_name='FORTRESS'),(SELECT id from town_building WHERE full_name='BASILISK_PIT'),(SELECT id from town_build_anim where full_name = 'UPGR_BASILISK_PIT'),5,0,5,0,0,0,2000),
            ((SELECT MAX(id) + 5 FROM town_building),'UPGR_GORGON_LAIR',(SELECT id FROM castle WHERE full_name='FORTRESS'),(SELECT id from town_building WHERE full_name='GORGON_LAIR'),(SELECT id from town_build_anim where full_name = 'UPGR_GORGON_LAIR'),5,0,5,0,0,0,2000),
            ((SELECT MAX(id) + 6 FROM town_building),'UPGR_WYVERN_NEST',(SELECT id FROM castle WHERE full_name='FORTRESS'),(SELECT id from town_building WHERE full_name='WYVERN_NEST'),(SELECT id from town_build_anim where full_name = 'UPGR_WYVERN_NEST'),10,10,0,0,0,0,3000),
            ((SELECT MAX(id) + 7 FROM town_building),'UPGR_HYDRA_POND',(SELECT id FROM castle WHERE full_name='FORTRESS'),(SELECT id from town_building WHERE full_name='HYDRA_POND'),(SELECT id from town_build_anim where full_name = 'UPGR_HYDRA_POND'),10,0,10,20,0,0,15000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'CAGE_OF_WARLORDS',17,2,0,80,60,1,703,36,1),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'EMPTY_CAPTAINS_QUARTERS',18,2,2,0,0,0,0,0,3),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'CAPTAINS_QUARTERS',18,2,2,144,147,1,641,121,3),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'UPGR_CAPTAINS_QUARTERS',19,2,2,159,200,1,641,68,3),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'GLYPHS_OF_FEAR',21,2,1,388,113,1,341,174,2),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'BLOOD_OBELISK',22,2,1,387,126,1,349,79,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'CAGE_OF_WARLORDS',(SELECT id FROM castle WHERE full_name='FORTRESS'),null,(SELECT id from town_build_anim where full_name = 'CAGE_OF_WARLORDS'),0,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 2 FROM town_building),'CAPTAINS_QUARTERS',(SELECT id FROM castle WHERE full_name='FORTRESS'),null,(SELECT id from town_build_anim where full_name = 'EMPTY_CAPTAINS_QUARTERS'),0,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'GLYPHS_OF_FEAR',(SELECT id FROM castle WHERE full_name='FORTRESS'),null,(SELECT id from town_build_anim where full_name = 'GLYPHS_OF_FEAR'),0,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 4 FROM town_building),'BLOOD_OBELISK',(SELECT id FROM castle WHERE full_name='FORTRESS'),null,(SELECT id from town_build_anim where full_name = 'BLOOD_OBELISK'),0,0,0,0,0,0,1000);

            insert into additional_build_anim (
            id, building_id, animation_id, existing_building_id
            ) values
            ((SELECT MAX(id) + 1 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'GNOLL_HUT'),(SELECT id FROM town_build_anim WHERE full_name = 'CAPTAINS_QUARTERS'),(SELECT id FROM town_building WHERE full_name = 'CAPTAINS_QUARTERS')),
            ((SELECT MAX(id) + 2 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'UPGR_GNOLL_HUT'),(SELECT id FROM town_build_anim WHERE full_name = 'UPGR_CAPTAINS_QUARTERS'),(SELECT id FROM town_building WHERE full_name = 'CAPTAINS_QUARTERS')),
            ((SELECT MAX(id) + 3 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'FORTRESS_VILLAGE_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'FORTRESS_LAKE'),null),
            ((SELECT MAX(id) + 4 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'FORTRESS_TOWN_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'FORTRESS_LAKE'),null),
            ((SELECT MAX(id) + 5 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'FORTRESS_CITY_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'FORTRESS_LAKE'),null),
            ((SELECT MAX(id) + 6 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'FORTRESS_CAPITOL'),(SELECT id FROM town_build_anim WHERE full_name = 'FORTRESS_LAKE'),null);

            insert into building_prereq (
            id, building_id, prereq_id
            ) values
            ((SELECT MAX(id) + 1 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'FORTRESS_TOWN_HALL'),(SELECT id FROM town_building WHERE full_name = 'FORTRESS_TAVERN')),
            ((SELECT MAX(id) + 2 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'FORTRESS_RESOURCE_SILO'),(SELECT id FROM town_building WHERE full_name = 'FORTRESS_MARKETPLACE')),
            ((SELECT MAX(id) + 3 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'FORTRESS_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'FORTRESS_MARKETPLACE')),
            ((SELECT MAX(id) + 4 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'FORTRESS_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'FORTRESS_MAGE_GUILD_1')),
            ((SELECT MAX(id) + 5 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'FORTRESS_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'FORTRESS_BLACKSMITH')),
            ((SELECT MAX(id) + 6 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'FORTRESS_CAPITOL'),(SELECT id FROM town_building WHERE full_name = 'FORTRESS_CASTLE')),
            ((SELECT MAX(id) + 7 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'CAGE_OF_WARLORDS'),(SELECT id FROM town_building WHERE full_name = 'FORTRESS_TOWN_HALL')),
            ((SELECT MAX(id) + 8 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'GLYPHS_OF_FEAR'),(SELECT id FROM town_building WHERE full_name = 'FORTRESS_FORT')),
            ((SELECT MAX(id) + 9 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'CAGE_OF_WARLORDS'),(SELECT id FROM town_building WHERE full_name = 'GLYPHS_OF_FEAR')),
            ((SELECT MAX(id) + 10 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'BLOOD_OBELISK'),(SELECT id FROM town_building WHERE full_name = 'GLYPHS_OF_FEAR')),
            ((SELECT MAX(id) + 11 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'GNOLL_HUT'),(SELECT id FROM town_building WHERE full_name = 'FORTRESS_FORT')),
            ((SELECT MAX(id) + 12 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'UPGR_GNOLL_HUT'),(SELECT id FROM town_building WHERE full_name = 'FORTRESS_TAVERN')),
            ((SELECT MAX(id) + 13 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'CAPTAINS_QUARTERS'),(SELECT id FROM town_building WHERE full_name = 'GNOLL_HUT')),
            ((SELECT MAX(id) + 14 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'LIZARD_DEN'),(SELECT id FROM town_building WHERE full_name = 'GNOLL_HUT')),
            ((SELECT MAX(id) + 15 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'SERPENT_FLY_HIVE'),(SELECT id FROM town_building WHERE full_name = 'GNOLL_HUT')),
            ((SELECT MAX(id) + 16 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'BASILISK_PIT'),(SELECT id FROM town_building WHERE full_name = 'SERPENT_FLY_HIVE')),
            ((SELECT MAX(id) + 17 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'GORGON_LAIR'),(SELECT id FROM town_building WHERE full_name = 'LIZARD_DEN')),
            ((SELECT MAX(id) + 18 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'GORGON_LAIR'),(SELECT id FROM town_building WHERE full_name = 'SERPENT_FLY_HIVE')),
            ((SELECT MAX(id) + 19 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'WYVERN_NEST'),(SELECT id FROM town_building WHERE full_name = 'LIZARD_DEN')),
            ((SELECT MAX(id) + 20 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'UPGR_GORGON_LAIR'),(SELECT id FROM town_building WHERE full_name = 'FORTRESS_RESOURCE_SILO')),
            ((SELECT MAX(id) + 21 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'HYDRA_POND'),(SELECT id FROM town_building WHERE full_name = 'BASILISK_PIT')),
            ((SELECT MAX(id) + 22 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'HYDRA_POND'),(SELECT id FROM town_building WHERE full_name = 'WYVERN_NEST'));

            -- CONFLUX
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'CONFLUX_VILLAGE_HALL',10,0,0,234,195,1,0,164,4),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'AURORA_BOREALIS',26,0,0,295,100,15,307,2,0),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'CONFLUX_FORT',7,0,1,359,151,1,349,101,0),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'CONFLUX_TAVERN',5,0,2,72,72,1,553,203,4),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'CONFLUX_BLACKSMITH',16,0,3,89,114,14,449,151,1),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'CONFLUX_MARKETPLACE',14,1,0,201,92,1,347,216,4),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'CONFLUX_MAGE_GUILD_1',0,1,1,85,237,1,206,58,3),
            ((SELECT MAX(id) + 8 FROM town_build_anim),'CONFLUX_SHIPYARD',6,1,2,108,49,1,239,215,2),
            ((SELECT MAX(id) + 9 FROM town_build_anim),'CONFLUX_VILLAGE_BUILDINGS',0,0,0,663,156,1,23,218,1),
            ((SELECT MAX(id) + 10 FROM town_build_anim),'CONFLUX_TOWN_BUILDINGS',0,0,0,492,169,1,232,205,1),
            ((SELECT MAX(id) + 11 FROM town_build_anim),'CONFLUX_CITY_BUILDINGS',0,0,0,234,81,1,516,223,1),
            ((SELECT MAX(id) + 12 FROM town_build_anim),'CONFLUX_CAPITOL_BUILDINGS',0,0,0,691,122,1,0,252,1),
            ((SELECT MAX(id) + 13 FROM town_build_anim),'CONFLUX_WATERFALL',0,0,0,44,75,10,682,183,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'CONFLUX_VILLAGE_HALL',(SELECT id FROM castle WHERE full_name='CONFLUX'),null,(SELECT id from town_build_anim where full_name = 'CONFLUX_VILLAGE_HALL'),0,0,0,0,0,0,0),
            ((SELECT MAX(id) + 2 FROM town_building),'AURORA_BOREALIS',(SELECT id FROM castle WHERE full_name='CONFLUX'),null,(SELECT id from town_build_anim where full_name = 'AURORA_BOREALIS'),0,0,0,0,0,0,0),
            ((SELECT MAX(id) + 3 FROM town_building),'CONFLUX_FORT',(SELECT id FROM castle WHERE full_name='CONFLUX'),null,(SELECT id from town_build_anim where full_name = 'CONFLUX_FORT'),20,0,20,0,0,0,5000),
            ((SELECT MAX(id) + 4 FROM town_building),'CONFLUX_TAVERN',(SELECT id FROM castle WHERE full_name='CONFLUX'),null,(SELECT id from town_build_anim where full_name = 'CONFLUX_TAVERN'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 5 FROM town_building),'CONFLUX_BLACKSMITH',(SELECT id FROM castle WHERE full_name='CONFLUX'),null,(SELECT id from town_build_anim where full_name = 'CONFLUX_BLACKSMITH'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 6 FROM town_building),'CONFLUX_MARKETPLACE',(SELECT id FROM castle WHERE full_name='CONFLUX'),null,(SELECT id from town_build_anim where full_name = 'CONFLUX_MARKETPLACE'),5,0,0,0,0,0,500),
            ((SELECT MAX(id) + 7 FROM town_building),'CONFLUX_MAGE_GUILD_1',(SELECT id FROM castle WHERE full_name='CONFLUX'),null,(SELECT id from town_build_anim where full_name = 'CONFLUX_MAGE_GUILD_1'),5,0,5,0,0,0,2000),
            ((SELECT MAX(id) + 8 FROM town_building),'CONFLUX_SHIPYARD',(SELECT id FROM castle WHERE full_name='CONFLUX'),null,(SELECT id from town_build_anim where full_name = 'CONFLUX_SHIPYARD'),20,0,0,0,0,0,2000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'CONFLUX_TOWN_HALL',11,0,0,234,195,1,0,164,4),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'CONFLUX_RESOURCE_SILO',15,1,0,68,114,1,372,171,2),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'CONFLUX_CITADEL',8,0,1,359,151,1,349,101,0),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'CONFLUX_MAGE_GUILD_2',1,1,1,85,237,1,206,58,3);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'CONFLUX_TOWN_HALL',(SELECT id FROM castle WHERE full_name = 'CONFLUX'),(SELECT id FROM town_building WHERE full_name='CONFLUX_VILLAGE_HALL'),(SELECT id from town_build_anim where full_name = 'CONFLUX_TOWN_HALL'),0,0,0,0,0,0,2500),
            ((SELECT MAX(id) + 2 FROM town_building),'CONFLUX_RESOURCE_SILO',(SELECT id FROM castle WHERE full_name = 'CONFLUX'),null,(SELECT id from town_build_anim where full_name = 'CONFLUX_RESOURCE_SILO'),0,0,5,0,0,0,5000),
            ((SELECT MAX(id) + 3 FROM town_building),'CONFLUX_CITADEL',(SELECT id FROM castle WHERE full_name = 'CONFLUX'),(SELECT id FROM town_building WHERE full_name='CONFLUX_FORT'),(SELECT id from town_build_anim where full_name = 'CONFLUX_CITADEL'),0,0,5,0,0,0,2500),
            ((SELECT MAX(id) + 4 FROM town_building),'CONFLUX_MAGE_GUILD_2',(SELECT id FROM castle WHERE full_name = 'CONFLUX'),(SELECT id FROM town_building WHERE full_name='CONFLUX_MAGE_GUILD_1'),(SELECT id from town_build_anim where full_name = 'CONFLUX_MAGE_GUILD_2'),5,4,5,4,4,4,5000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'CONFLUX_CITY_HALL',12,0,0,234,195,1,0,164,4),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'CONFLUX_CASTLE',9,0,1,359,151,1,349,101,0),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'CONFLUX_MAGE_GUILD_3',2,1,1,85,237,1,206,58,3);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'CONFLUX_CITY_HALL',(SELECT id FROM castle WHERE full_name = 'CONFLUX'),(SELECT id FROM town_building WHERE full_name='CONFLUX_TOWN_HALL'),(SELECT id from town_build_anim where full_name = 'CONFLUX_CITY_HALL'),0,0,0,0,0,0,5000),
            ((SELECT MAX(id) + 2 FROM town_building),'CONFLUX_CASTLE',(SELECT id FROM castle WHERE full_name = 'CONFLUX'),(SELECT id FROM town_building WHERE full_name='CONFLUX_CITADEL'),(SELECT id from town_build_anim where full_name = 'CONFLUX_CASTLE'),10,0,10,0,0,0,5000),
            ((SELECT MAX(id) + 3 FROM town_building),'CONFLUX_MAGE_GUILD_3',(SELECT id FROM castle WHERE full_name = 'CONFLUX'),(SELECT id FROM town_building WHERE full_name='CONFLUX_MAGE_GUILD_2'),(SELECT id from town_build_anim where full_name = 'CONFLUX_MAGE_GUILD_3'),5,6,5,6,6,6,1000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'CONFLUX_CAPITOL',13,0,0,234,195,1,0,164,4),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'CONFLUX_MAGE_GUILD_4',3,1,1,85,237,1,206,58,3);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'CONFLUX_CAPITOL',(SELECT id FROM castle WHERE full_name = 'CONFLUX'),(SELECT id FROM town_building WHERE full_name='CONFLUX_CITY_HALL'),(SELECT id from town_build_anim where full_name = 'CONFLUX_CAPITOL'),0,0,0,0,0,0,10000),
            ((SELECT MAX(id) + 2 FROM town_building),'CONFLUX_MAGE_GUILD_4',(SELECT id FROM castle WHERE full_name = 'CONFLUX'),(SELECT id FROM town_building WHERE full_name='CONFLUX_MAGE_GUILD_3'),(SELECT id from town_build_anim where full_name = 'CONFLUX_MAGE_GUILD_4'),5,8,5,8,8,8,1000);
            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'CONFLUX_MAGE_GUILD_5',4,1,1,85,237,1,206,58,3);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'CONFLUX_MAGE_GUILD_5',(SELECT id FROM castle WHERE full_name = 'CONFLUX'),(SELECT id FROM town_building WHERE full_name='CONFLUX_MAGE_GUILD_4'),(SELECT id from town_build_anim where full_name = 'CONFLUX_MAGE_GUILD_5'),5,10,5,10,10,10,1000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'MAGIC_LANTERN',30,3,0,111,124,10,689,250,4),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'ALTAR_OF_AIR',31,3,1,90,70,15,630,50,0),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'ALTAR_OF_WATER',32,3,2,80,38,8,709,210,2),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'ALTAR_OF_FIRE',33,3,3,89,79,8,108,131,1),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'ALTAR_OF_EARTH',34,4,0,70,45,8,264,168,0),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'ALTAR_OF_THOUGHT',35,4,1,86,88,20,394,283,5),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'PYRE',36,4,2,156,126,19,34,16,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'MAGIC_LANTERN',(SELECT id FROM castle WHERE full_name='CONFLUX'),null,(SELECT id from town_build_anim where full_name = 'MAGIC_LANTERN'),5,0,5,0,0,0,300),
            ((SELECT MAX(id) + 2 FROM town_building),'ALTAR_OF_AIR',(SELECT id FROM castle WHERE full_name='CONFLUX'),null,(SELECT id from town_build_anim where full_name = 'ALTAR_OF_AIR'),0,0,5,0,0,0,1500),
            ((SELECT MAX(id) + 3 FROM town_building),'ALTAR_OF_WATER',(SELECT id FROM castle WHERE full_name='CONFLUX'),null,(SELECT id from town_build_anim where full_name = 'ALTAR_OF_WATER'),0,0,5,0,0,0,1500),
            ((SELECT MAX(id) + 4 FROM town_building),'ALTAR_OF_FIRE',(SELECT id FROM castle WHERE full_name='CONFLUX'),null,(SELECT id from town_build_anim where full_name = 'ALTAR_OF_FIRE'),5,0,5,0,0,0,2000),
            ((SELECT MAX(id) + 5 FROM town_building),'ALTAR_OF_EARTH',(SELECT id FROM castle WHERE full_name='CONFLUX'),null,(SELECT id from town_build_anim where full_name = 'ALTAR_OF_EARTH'),0,0,10,0,0,0,2000),
            ((SELECT MAX(id) + 6 FROM town_building),'ALTAR_OF_THOUGHT',(SELECT id FROM castle WHERE full_name='CONFLUX'),null,(SELECT id from town_build_anim where full_name = 'ALTAR_OF_THOUGHT'),5,2,5,2,2,2,3000),
            ((SELECT MAX(id) + 7 FROM town_building),'PYRE',(SELECT id FROM castle WHERE full_name='CONFLUX'),null,(SELECT id from town_build_anim where full_name = 'PYRE'),10,10,10,0,0,0,10000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'UPGR_MAGIC_LANTERN',37,3,0,111,124,10,689,250,4),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'UPGR_ALTAR_OF_AIR',38,3,1,90,70,15,630,50,0),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'UPGR_ALTAR_OF_WATER',39,3,2,80,38,8,709,210,2),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'UPGR_ALTAR_OF_FIRE',40,3,3,89,79,8,108,131,1),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'UPGR_ALTAR_OF_EARTH',41,4,0,70,45,8,264,168,0),
            ((SELECT MAX(id) + 6 FROM town_build_anim),'UPGR_ALTAR_OF_THOUGHT',42,4,1,86,88,21,394,283,5),
            ((SELECT MAX(id) + 7 FROM town_build_anim),'UPGR_PYRE',43,4,2,156,142,19,34,0,0);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'UPGR_MAGIC_LANTERN',(SELECT id FROM castle WHERE full_name='CONFLUX'),(SELECT id from town_building WHERE full_name='MAGIC_LANTERN'),(SELECT id from town_build_anim where full_name = 'UPGR_MAGIC_LANTERN'),0,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 2 FROM town_building),'UPGR_ALTAR_OF_AIR',(SELECT id FROM castle WHERE full_name='CONFLUX'),(SELECT id from town_building WHERE full_name='ALTAR_OF_AIR'),(SELECT id from town_build_anim where full_name = 'UPGR_ALTAR_OF_AIR'),2,2,0,0,0,2,1500),
            ((SELECT MAX(id) + 3 FROM town_building),'UPGR_ALTAR_OF_WATER',(SELECT id FROM castle WHERE full_name='CONFLUX'),(SELECT id from town_building WHERE full_name='ALTAR_OF_WATER'),(SELECT id from town_build_anim where full_name = 'UPGR_ALTAR_OF_WATER'),0,5,5,0,0,0,2000),
            ((SELECT MAX(id) + 4 FROM town_building),'UPGR_ALTAR_OF_FIRE',(SELECT id FROM castle WHERE full_name='CONFLUX'),(SELECT id from town_building WHERE full_name='ALTAR_OF_FIRE'),(SELECT id from town_build_anim where full_name = 'UPGR_ALTAR_OF_FIRE'),0,5,5,0,0,0,2000),
            ((SELECT MAX(id) + 5 FROM town_building),'UPGR_ALTAR_OF_EARTH',(SELECT id FROM castle WHERE full_name='CONFLUX'),(SELECT id from town_building WHERE full_name='ALTAR_OF_EARTH'),(SELECT id from town_build_anim where full_name = 'UPGR_ALTAR_OF_EARTH'),0,0,0,5,0,0,1000),
            ((SELECT MAX(id) + 6 FROM town_building),'UPGR_ALTAR_OF_THOUGHT',(SELECT id FROM castle WHERE full_name='CONFLUX'),(SELECT id from town_building WHERE full_name='ALTAR_OF_THOUGHT'),(SELECT id from town_build_anim where full_name = 'UPGR_ALTAR_OF_THOUGHT'),0,3,0,3,3,3,3000),
            ((SELECT MAX(id) + 7 FROM town_building),'UPGR_PYRE',(SELECT id FROM castle WHERE full_name='CONFLUX'),(SELECT id from town_building WHERE full_name='PYRE'),(SELECT id from town_build_anim where full_name = 'UPGR_PYRE'),10,20,10,0,0,0,15000);

            insert into town_build_anim (
            id, full_name, hall_picture_id, hall_picture_row, hall_picture_col, picture_width, picture_height, picture_count, picture_left, picture_top, picture_layer
            ) values
            ((SELECT MAX(id) + 1 FROM town_build_anim),'CONFLUX_ARTIFACT_MERCHANTS',17,2,1,118,95,1,284,246,4),
            ((SELECT MAX(id) + 2 FROM town_build_anim),'EMPTY_GARDEN_OF_LIFE',18,2,2,0,0,0,0,0,4),
            ((SELECT MAX(id) + 3 FROM town_build_anim),'GARDEN_OF_LIFE',18,2,2,111,124,10,689,250,4),
            ((SELECT MAX(id) + 4 FROM town_build_anim),'UPGR_GARDEN_OF_LIFE',19,2,2,111,124,10,689,250,4),
            ((SELECT MAX(id) + 5 FROM town_build_anim),'MAGIC_UNIVERSITY',21,2,0,185,119,1,104,170,2);
            insert into town_building (
            id, full_name, castle_id, parent_id, animation_id, wood, mercury, ore, sulfur, crystal, gems, gold
            ) values
            ((SELECT MAX(id) + 1 FROM town_building),'CONFLUX_ARTIFACT_MERCHANTS',(SELECT id FROM castle WHERE full_name='CONFLUX'),null,(SELECT id from town_build_anim where full_name = 'CONFLUX_ARTIFACT_MERCHANTS'),0,0,0,0,0,0,10000),
            ((SELECT MAX(id) + 2 FROM town_building),'GARDEN_OF_LIFE',(SELECT id FROM castle WHERE full_name='CONFLUX'),null,(SELECT id from town_build_anim where full_name = 'EMPTY_GARDEN_OF_LIFE'),0,0,0,0,0,0,1000),
            ((SELECT MAX(id) + 3 FROM town_building),'MAGIC_UNIVERSITY',(SELECT id FROM castle WHERE full_name='CONFLUX'),null,(SELECT id from town_build_anim where full_name = 'MAGIC_UNIVERSITY'),10,0,10,0,0,0,5000);

            insert into additional_build_anim (
            id, building_id, animation_id, existing_building_id
            ) values
            ((SELECT MAX(id) + 1 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_VILLAGE_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'CONFLUX_VILLAGE_BUILDINGS'),null),
            ((SELECT MAX(id) + 2 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_TOWN_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'CONFLUX_VILLAGE_BUILDINGS'),null),
            ((SELECT MAX(id) + 3 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_TOWN_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'CONFLUX_TOWN_BUILDINGS'),null),
            ((SELECT MAX(id) + 4 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_CITY_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'CONFLUX_VILLAGE_BUILDINGS'),null),
            ((SELECT MAX(id) + 5 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_CITY_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'CONFLUX_TOWN_BUILDINGS'),null),
            ((SELECT MAX(id) + 6 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_CITY_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'CONFLUX_CITY_BUILDINGS'),null),
            ((SELECT MAX(id) + 7 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_CAPITOL'),(SELECT id FROM town_build_anim WHERE full_name = 'CONFLUX_VILLAGE_BUILDINGS'),null),
            ((SELECT MAX(id) + 8 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_CAPITOL'),(SELECT id FROM town_build_anim WHERE full_name = 'CONFLUX_TOWN_BUILDINGS'),null),
            ((SELECT MAX(id) + 9 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_CAPITOL'),(SELECT id FROM town_build_anim WHERE full_name = 'CONFLUX_CITY_BUILDINGS'),null),
            ((SELECT MAX(id) + 10 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_CAPITOL'),(SELECT id FROM town_build_anim WHERE full_name = 'CONFLUX_CAPITOL_BUILDINGS'),null),
            ((SELECT MAX(id) + 11 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_VILLAGE_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'CONFLUX_WATERFALL'),null),
            ((SELECT MAX(id) + 12 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_TOWN_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'CONFLUX_WATERFALL'),null),
            ((SELECT MAX(id) + 13 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_CITY_HALL'),(SELECT id FROM town_build_anim WHERE full_name = 'CONFLUX_WATERFALL'),null),
            ((SELECT MAX(id) + 14 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_CAPITOL'),(SELECT id FROM town_build_anim WHERE full_name = 'CONFLUX_WATERFALL'),null),
            ((SELECT MAX(id) + 15 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'MAGIC_LANTERN'),(SELECT id FROM town_build_anim WHERE full_name = 'GARDEN_OF_LIFE'),(SELECT id FROM town_building WHERE full_name = 'GARDEN_OF_LIFE')),
            ((SELECT MAX(id) + 16 FROM additional_build_anim),(SELECT id FROM town_building WHERE full_name = 'UPGR_MAGIC_LANTERN'),(SELECT id FROM town_build_anim WHERE full_name = 'UPGR_GARDEN_OF_LIFE'),(SELECT id FROM town_building WHERE full_name = 'GARDEN_OF_LIFE'));

            insert into building_prereq (
            id, building_id, prereq_id
            ) values
            ((SELECT MAX(id) + 1 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_TOWN_HALL'),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_TAVERN')),
            ((SELECT MAX(id) + 2 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_RESOURCE_SILO'),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_MARKETPLACE')),
            ((SELECT MAX(id) + 3 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_MARKETPLACE')),
            ((SELECT MAX(id) + 4 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_MAGE_GUILD_1')),
            ((SELECT MAX(id) + 5 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_CITY_HALL'),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_BLACKSMITH')),
            ((SELECT MAX(id) + 6 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_CAPITOL'),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_CASTLE')),
            ((SELECT MAX(id) + 7 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_ARTIFACT_MERCHANTS'),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_MARKETPLACE')),
            ((SELECT MAX(id) + 8 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'MAGIC_UNIVERSITY'),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_MAGE_GUILD_1')),
            ((SELECT MAX(id) + 9 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'MAGIC_LANTERN'),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_FORT')),
            ((SELECT MAX(id) + 10 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'ALTAR_OF_AIR'),(SELECT id FROM town_building WHERE full_name = 'MAGIC_LANTERN')),
            ((SELECT MAX(id) + 11 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'ALTAR_OF_WATER'),(SELECT id FROM town_building WHERE full_name = 'MAGIC_LANTERN')),
            ((SELECT MAX(id) + 12 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'GARDEN_OF_LIFE'),(SELECT id FROM town_building WHERE full_name = 'MAGIC_LANTERN')),
            ((SELECT MAX(id) + 13 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'ALTAR_OF_AIR'),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_MAGE_GUILD_1')),
            ((SELECT MAX(id) + 14 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'ALTAR_OF_WATER'),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_MAGE_GUILD_1')),
            ((SELECT MAX(id) + 15 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'ALTAR_OF_FIRE'),(SELECT id FROM town_building WHERE full_name = 'ALTAR_OF_AIR')),
            ((SELECT MAX(id) + 16 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'ALTAR_OF_EARTH'),(SELECT id FROM town_building WHERE full_name = 'ALTAR_OF_WATER')),
            ((SELECT MAX(id) + 17 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'UPGR_ALTAR_OF_FIRE'),(SELECT id FROM town_building WHERE full_name = 'UPGR_ALTAR_OF_AIR')),
            ((SELECT MAX(id) + 18 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'ALTAR_OF_THOUGHT'),(SELECT id FROM town_building WHERE full_name = 'ALTAR_OF_FIRE')),
            ((SELECT MAX(id) + 19 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'ALTAR_OF_THOUGHT'),(SELECT id FROM town_building WHERE full_name = 'ALTAR_OF_EARTH')),
            ((SELECT MAX(id) + 20 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'UPGR_ALTAR_OF_THOUGHT'),(SELECT id FROM town_building WHERE full_name = 'CONFLUX_MAGE_GUILD_2')),
            ((SELECT MAX(id) + 21 FROM building_prereq),(SELECT id FROM town_building WHERE full_name = 'PYRE'),(SELECT id FROM town_building WHERE full_name = 'ALTAR_OF_THOUGHT'));