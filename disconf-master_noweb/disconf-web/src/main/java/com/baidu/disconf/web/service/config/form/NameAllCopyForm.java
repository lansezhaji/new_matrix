package com.baidu.disconf.web.service.config.form;

public class NameAllCopyForm {

    private String versionNameCopySource;

    private String envIdCopySource;

    private String versionNameTarget;

    private String envIdCopyTarget;

    public String getVersionNameCopySource() {
        return versionNameCopySource;
    }

    public void setVersionNameCopySource(String versionNameCopySource) {
        this.versionNameCopySource = versionNameCopySource;
    }

    public String getEnvIdCopySource() {
        return envIdCopySource;
    }

    public void setEnvIdCopySource(String envIdCopySource) {
        this.envIdCopySource = envIdCopySource;
    }

    public String getVersionNameTarget() {
        return versionNameTarget;
    }

    public void setVersionNameTarget(String versionNameTarget) {
        this.versionNameTarget = versionNameTarget;
    }

    public String getEnvIdCopyTarget() {
        return envIdCopyTarget;
    }

    public void setEnvIdCopyTarget(String envIdCopyTarget) {
        this.envIdCopyTarget = envIdCopyTarget;
    }


}
