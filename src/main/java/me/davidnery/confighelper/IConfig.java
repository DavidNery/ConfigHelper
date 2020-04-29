package me.davidnery.confighelper;

import me.davidnery.confighelper.exceptions.ConfigNotValid;
import me.davidnery.confighelper.exceptions.IncompatibleKeyType;
import me.davidnery.confighelper.exceptions.SectionNotValid;
import me.davidnery.confighelper.models.sections.BaseSection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface IConfig {

    /**
     * Load config from file with name stored in {@code defaultName}
     */
    void load() throws FileNotFoundException, ConfigNotValid, IncompatibleKeyType;

    /**
     * Load config from file
     *
     * @param file the file
     */
    void load(File file) throws FileNotFoundException, ConfigNotValid, IncompatibleKeyType;

    /**
     * Load config from file with name stored in {@code defaultName} to a section
     *
     * @param section the destination section
     */
    void loadToSection(BaseSection section) throws FileNotFoundException, ConfigNotValid, SectionNotValid;

    /**
     * Load config from a file to a section
     *
     * @param file    the source file
     * @param section the destination section
     */
    void loadToSection(File file, BaseSection section) throws FileNotFoundException, ConfigNotValid, SectionNotValid;

    /**
     * Save config to file with name stored in {@code defaultName}
     */
    void save() throws IOException;

    /**
     * Save config to a file
     *
     * @param file the file
     */
    void save(File file) throws IOException;

    /**
     * Save a section to file with name stored in {@code defaultName}
     *
     * @param section the source section
     */
    void saveFromSection(BaseSection section) throws IOException;

    /**
     * Save a section to a file
     *
     * @param section the source section
     * @param file    the destination file
     */
    void saveFromSection(BaseSection section, File file) throws IOException;

}
