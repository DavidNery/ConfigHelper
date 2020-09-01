package me.davidnery.confighelper.types.yaml;

import me.davidnery.confighelper.IConfig;
import me.davidnery.confighelper.exceptions.ConfigNotValid;
import me.davidnery.confighelper.exceptions.IncompatibleKeyType;
import me.davidnery.confighelper.exceptions.SectionNotValid;
import me.davidnery.confighelper.models.sections.BaseSection;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;

public abstract class YAMLConfiguration extends YAMLSerialization implements IConfig {

    protected String defaultName;

    protected boolean replaceIfExists;

    public YAMLConfiguration(String defaultName) {
        this(defaultName, false);
    }

    public YAMLConfiguration(boolean replaceIfExists) {
        this("config.yml", replaceIfExists);
    }

    public YAMLConfiguration(String defaultName, boolean replaceIfExists) {
        this.defaultName = defaultName;
        this.replaceIfExists = replaceIfExists;
    }

    @Override
    public void load() throws FileNotFoundException, ConfigNotValid, IncompatibleKeyType {
        load(new File(defaultName));
    }

    @Override
    public void load(File file) throws FileNotFoundException, ConfigNotValid, IncompatibleKeyType {
        new YAMLUtils().setup(this, (LinkedHashMap<String, Object>) getConfig(file));
    }

    @Override
    public void loadToSection(BaseSection section) throws FileNotFoundException, ConfigNotValid, SectionNotValid {
        loadToSection(new File(defaultName), section);
    }

    @Override
    public void loadToSection(File file, BaseSection section) throws FileNotFoundException, ConfigNotValid, SectionNotValid {
        new YAMLUtils().setMapSection(getConfig(file), section);
    }

    @Override
    public void save() throws IOException {
        save(new File(defaultName));
    }

    @Override
    public void save(File file) throws IOException {
        save(file, new YAMLUtils().process(this));
    }

    @Override
    public void saveFromSection(BaseSection section) throws IOException {
        saveFromSection(section, new File(defaultName));
    }

    @Override
    public void saveFromSection(BaseSection section, File file) throws IOException {
        save(file, section.get());
    }

    private Object getConfig(File file) throws FileNotFoundException, ConfigNotValid {
        if (!file.exists()) throw new FileNotFoundException();

        InputStream inputStream = new FileInputStream(file);
        Object config = new Yaml().load(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        if (!(config instanceof LinkedHashMap)) throw new ConfigNotValid();

        return config;
    }

    private void save(File file, Object object) throws IOException {
        boolean created = false;
        if (!file.exists())
            created = file.createNewFile();

        if (replaceIfExists || created) {
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            new Yaml(options).dump(object, new OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8));

            fileOutputStream.flush();
            fileOutputStream.close();
        }
    }
}
