package com.konsol.divinesign;

import com.konsol.divinesign.config.AsyncSyncConfiguration;
import com.konsol.divinesign.config.EmbeddedMongo;
import com.konsol.divinesign.config.JacksonConfiguration;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = { DivineSignApp.class, JacksonConfiguration.class, AsyncSyncConfiguration.class })
@EmbeddedMongo
public @interface IntegrationTest {
}
