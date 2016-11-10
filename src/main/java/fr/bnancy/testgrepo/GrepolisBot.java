package fr.bnancy.testgrepo;

import fr.bnancy.testgrepo.data.*;
import fr.bnancy.testgrepo.data.builders.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by bertrand on 8/31/16.
 */
@SpringBootApplication
public class GrepolisBot implements CommandLineRunner {

    protected final Logger logger = Logger.getLogger(this.getClass());

    @Autowired
    private GrepoApi api;

    private Player player;

    Runnable fetchDataTask, notifyTask;

    @Override
    public void run(String... args) throws Exception {
        String result = api.login("", "", ""); // Enter creds here
        if (result != "success") {
            logger.error("Couldn't login !");
            return;
        } else {
            initTasks();
        }
    }

    private void initTasks() {

        fetchDataTask = () -> {
            try {
                logger.info("Initial data fetch from server");
                JsonObject data = api.fetchData();

                buildPlayer(data);

                String collections[] = {
                        "Towns", "Units"
                };
//                logger.warn(api.fetchCollections(collections));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        notifyTask = () -> {
            try {
                logger.info("Sending notify request");
                api.notifyRequest();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        executorService.scheduleWithFixedDelay(fetchDataTask, 0, 30, TimeUnit.SECONDS);
        executorService.scheduleWithFixedDelay(notifyTask, 300, 300, TimeUnit.SECONDS);
    }

    private void buildPlayer(JsonObject data) throws Exception {

        JsonObject initialGameData = api.getGameData();

        int playerId = initialGameData.getInt("player_id");
        String playerName = initialGameData.getString("player_name");

        JsonArray models = data.getJsonArray("models");
        JsonArray collections = data.getJsonArray("collections");

        JsonObject attackSpot = (JsonObject) models.stream()
                .filter(x -> ((JsonObject) x).getString("model_class_name").equals("PlayerAttackSpot"))
                .findFirst()
                .get();

//        JsonObject gods = (JsonObject) models.stream()
//                .filter(x -> ((JsonObject) x).getString("model_class_name").equals("PlayerGods"))
//                .findFirst()
//                .get();
//
//        JsonObject commands = (JsonObject) models.stream()
//                .filter(x -> ((JsonObject) x).getString("model_class_name").equals("CommandsMenuBubble"))
//                .findFirst()
//                .get();

        JsonObject buildings = (JsonObject) models.stream()
                .filter(x -> ((JsonObject) x).getString("model_class_name").equals("BuildingBuildData"))
                .findFirst()
                .get();

        JsonObject town = (JsonObject) models.stream()
                .filter(x -> ((JsonObject) x).getString("model_class_name").equals("Town"))
                .findFirst()
                .get();

//        JsonObject building_queue = (JsonObject) collections.stream()
//                .filter(x -> ((JsonObject) x).getString("class_name").equals("BuildingOrders"))
//                .findFirst()
//                .get();
//
//        JsonObject units_queue = (JsonObject) collections.stream()
//                .filter(x -> ((JsonObject) x).getString("class_name").equals("RemainingUnitOrders"))
//                .findFirst()
//                .get();
//
        JsonObject farmTowns = (JsonObject) collections.stream()
                .filter(x -> ((JsonObject) x).getString("class_name").equals("FarmTowns"))
                .findFirst()
                .get();

        JsonObject farmTownPlayerRelation = (JsonObject) collections.stream()
                .filter(x -> ((JsonObject) x).getString("class_name").equals("FarmTownPlayerRelations"))
                .findFirst()
                .get();

        JsonObject units = api.fetchCollection("Units");


        player = new PlayerBuilder()
                .setId((long) playerId)
                .setName(playerName)
                .setCurrentTown(
                        new TownBuilder()
                            .setId((long) town.getJsonObject("data").getInt("id"))
                                .setIron(town.getJsonObject("data").getJsonObject("resources").getInt("iron"))
                                .setWood(town.getJsonObject("data").getJsonObject("resources").getInt("wood"))
                                .setStone(town.getJsonObject("data").getJsonObject("resources").getInt("stone"))
                                .setAttackSpot(
                                        new AttackSpotBuilder()
                                                .setId(attackSpot.getJsonObject("data").getInt("id"))
                                                .setLevel(attackSpot.getJsonObject("data").getInt("level"))
                                                .setNextFetch((long) attackSpot.getJsonObject("data").getInt("cooldown_at"))
                                                .createAttackSpot()
                                )
                                .setBuildings(
                                    buildings.getJsonObject("data")
                                            .getJsonObject("building_data")
                                            .entrySet().stream()
                                                .collect(Collectors.toMap(p -> p.getKey(), p -> new BuildingBuilder()
                                                        .setName(p.getKey())
                                                        .setLevel(((JsonObject)p.getValue()).getInt("level"))
                                                        .createBuilding())
                                                )
                                )
                                .setUnits(
                                    units.entrySet().stream()
                                            .filter(p -> Arrays.asList(Unit.UNITS_NAME).contains(p.getKey()))
                                            .collect(Collectors.toMap(p -> p.getKey(), p -> new UnitBuilder()
                                                    .setName(p.getKey())
                                                    .setQuantity(Integer.valueOf(p.getValue().toString()))
                                                    .createUnit())
                                            )
                                )
                            .createTown()
                )
                .createPlayer();

        logger.debug(player);
    }
}
