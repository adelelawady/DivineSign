package com.konsol.divinesign.config.dbmigrations;

import com.konsol.divinesign.config.Constants;
import com.konsol.divinesign.domain.Authority;
import com.konsol.divinesign.domain.Surah;
import com.konsol.divinesign.domain.User;
import com.konsol.divinesign.domain.Verse;
import com.konsol.divinesign.security.AuthoritiesConstants;
import io.mongock.api.annotations.ChangeUnit;
import io.mongock.api.annotations.Execution;
import io.mongock.api.annotations.RollbackExecution;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Creates the initial database setup.
 */
@ChangeUnit(id = "users-initialization", order = "001")
public class InitialSetupMigration {

    private final MongoTemplate template;

    public InitialSetupMigration(MongoTemplate template) {
        this.template = template;
    }

    @Execution
    public void changeSet() {
        Authority userAuthority = createUserAuthority();
        userAuthority = template.save(userAuthority);
        Authority adminAuthority = createAdminAuthority();
        adminAuthority = template.save(adminAuthority);
        addUsers(userAuthority, adminAuthority);
        try {
            loadQuranDB();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @RollbackExecution
    public void rollback() {}

    private Authority createAuthority(String authority) {
        Authority adminAuthority = new Authority();
        adminAuthority.setName(authority);
        return adminAuthority;
    }

    private Authority createAdminAuthority() {
        Authority adminAuthority = createAuthority(AuthoritiesConstants.ADMIN);
        return adminAuthority;
    }

    private Authority createUserAuthority() {
        Authority userAuthority = createAuthority(AuthoritiesConstants.USER);
        return userAuthority;
    }

    private void addUsers(Authority userAuthority, Authority adminAuthority) {
        User user = createUser(userAuthority);
        template.save(user);
        User admin = createAdmin(adminAuthority, userAuthority);
        template.save(admin);
    }

    private User createUser(Authority userAuthority) {
        User userUser = new User();
        userUser.setId("user-2");
        userUser.setLogin("user");
        userUser.setPassword("$2a$10$VEjxo0jq2YG9Rbk2HmX9S.k1uZBGYUHdUcid3g/vfiEl7lwWgOH/K");
        userUser.setFirstName("User");
        userUser.setLastName("User");
        userUser.setEmail("user@localhost");
        userUser.setActivated(true);
        userUser.setLangKey("en");
        userUser.setCreatedBy(Constants.SYSTEM);
        userUser.setCreatedDate(Instant.now());
        userUser.getAuthorities().add(userAuthority);
        return userUser;
    }

    private User createAdmin(Authority adminAuthority, Authority userAuthority) {
        User adminUser = new User();
        adminUser.setId("user-1");
        adminUser.setLogin("admin");
        adminUser.setPassword("$2a$10$gSAhZrxMllrbgj/kkK9UceBPpChGWJA7SYIb1Mqo.n5aNLq1/oRrC");
        adminUser.setFirstName("admin");
        adminUser.setLastName("Administrator");
        adminUser.setEmail("admin@localhost");
        adminUser.setActivated(true);
        adminUser.setLangKey("en");
        adminUser.setCreatedBy(Constants.SYSTEM);
        adminUser.setCreatedDate(Instant.now());
        adminUser.getAuthorities().add(adminAuthority);
        adminUser.getAuthorities().add(userAuthority);
        return adminUser;
    }

    public void loadQuranDB() throws Exception {
        InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("quran/quran.json");

        assert in != null;
        InputStreamReader isReader = new InputStreamReader(in, StandardCharsets.UTF_8);

        BufferedReader reader = new BufferedReader(isReader);
        StringBuilder sb = new java.lang.StringBuilder();
        String str;
        try {
            while ((str = reader.readLine()) != null) {
                sb.append(str);
            }
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        JSONArray jsonarray = new JSONArray(sb.toString());
        System.out.println(jsonarray.toString());

        for (int i = 0; i < jsonarray.length(); i++) {
            JSONObject jsonobject = jsonarray.getJSONObject(i);

            Surah ver = new Surah();

            String id = String.valueOf(jsonobject.getInt("id"));
            String name = jsonobject.getString("name");
            String transliteration = jsonobject.getString("transliteration");
            String type = jsonobject.getString("type");
            String total_verses = String.valueOf(jsonobject.getInt("total_verses"));

            ver.setSurahId(id);
            ver.setName(name);
            ver.setTransliteration(transliteration);
            ver.setType(type);
            ver.setTotalVerses(Integer.valueOf(total_verses));

            Surah savedVerse = template.save(ver);

            JSONArray subVerses = jsonobject.getJSONArray("verses");

            for (int ii = 0; ii < subVerses.length(); ii++) {
                try {
                    JSONObject subJsonobject = subVerses.getJSONObject(ii);

                    Verse subVerse = new Verse();

                    String subId = String.valueOf(subJsonobject.getInt("id"));
                    String subText = subJsonobject.getString("text");
                    subVerse.setVerseId(subId);
                    subVerse.setVerse(subText);

                    String subRemovedFormate = removeDiacritics(subText);

                    subRemovedFormate = subRemovedFormate
                        .replaceAll("[أإآاٱ]", "ا")
                        .replace("لآ", "لا")
                        .replace("آ", "ا")
                        .replace("نٞ", "ن")
                        .replace("بٞ", "ب")
                        .replace("رٞ", "ر")
                        .replace("رٗٞ", "ر")
                        .replace("مٞ", "م")
                        .replace("وٓ", "و")
                        .replace("دٗٓ", "د")
                        .replace("وٞٓ", "و")
                        .replace("ةٗٓ", "ة")
                        .replace("يـٓٗٓ", "يـ")
                        .replace("ىٓٞٓ", "ي")
                        .replace("هٓ", "ه")
                        .replace("لٗا", "لا")
                        .replace("لٗا", "لا")
                        .replace("نٗا", "نا")
                        .replace("رٗ", "ر")
                        .replace("ءٞ", "ء")
                        .replace("دٞ", "د")
                        .replace("ةٗ", "ة")
                        .replace("ءٞ", "ء")
                        .replace("دٞ", "د")
                        .replace("يٞ", "ي")
                        .replace("قٞ", "ق")
                        .replace("هـٓؤ", "هؤ")
                        .replace("هـٓا", "ها")
                        .replace("لـٓئك", "لئك")
                        .replace("ةٞ", "ة")
                        .replace("ضٞ", "ض")
                        .replace("اليل", "الليل")
                        .replace("سٞ", "س")
                        .replace("بٗا", "با")
                        .replace("تٞ", "ت")
                        .replace("يٓ", "ي")
                        .replace("ةٖ", "ة")
                        .replace("رٖ", "ر")
                        .replace("ءٖ", "ء")
                        .replace("ةٖ", "ة")
                        .replace("رٖ", "ر")
                        .replace("ءٖ", "ء")
                        .replace("ثٞ", "ث")
                        .replace("كٞ", "ك")
                        .replace("لٞ", "ل")
                        .replace("مٖ", "م");
                    subVerse.setDiacriticVerse(subRemovedFormate);

                    subVerse.setSurah(savedVerse);
                    Verse savedSubVerse = template.save(subVerse);
                    savedSubVerse.setId(subId);
                    savedVerse.addVerses(savedSubVerse);
                    template.save(savedVerse);

                    System.out.println(savedSubVerse.toString());
                } catch (Exception e) {
                    throw new Exception(e.getMessage());
                }
            }
        }
    }

    private static final String DIACRITICS_REGEX = "[\\u064B-\\u0652\\u0670\\u06D6-\\u06ED]";

    public static String removeDiacritics(String input) {
        if (input == null) {
            return null;
        }
        return input.replaceAll(DIACRITICS_REGEX, "");
    }
}
