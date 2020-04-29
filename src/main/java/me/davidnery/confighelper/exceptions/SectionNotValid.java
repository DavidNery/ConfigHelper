package me.davidnery.confighelper.exceptions;

public class SectionNotValid extends Exception {

    public SectionNotValid() {
        super("The section must be extends the BaseSection class");
    }
}
