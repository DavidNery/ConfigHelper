package me.davidnery.confighelper.exceptions;

public class ConfigNotValid extends Exception {

    public ConfigNotValid() {
        super("The config file must be have a group of keys and values");
    }
}
