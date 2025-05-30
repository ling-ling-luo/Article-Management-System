package com.example.article.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private Database db = new Database();

    public AppProperties() {}
    public AppProperties(Database db) { this.db = db; }
    public Database getDb() { return db; }
    public void setDb(Database db) { this.db = db; }

    public static class Database {
        private Initialization init = new Initialization();
        public Database() {}
        public Database(Initialization init) { this.init = init; }
        public Initialization getInit() { return init; }
        public void setInit(Initialization init) { this.init = init; }
        public static class Initialization {
            private boolean enabled = false;
            public Initialization() {}
            public Initialization(boolean enabled) { this.enabled = enabled; }
            public boolean isEnabled() { return enabled; }
            public void setEnabled(boolean enabled) { this.enabled = enabled; }
        }
    }
} 