import groovy.json.JsonSlurper
import com.stackstate.stackpack.ProvisioningScript
import com.stackstate.stackpack.ProvisioningContext
import com.stackstate.stackpack.ProvisioningIO
import com.stackstate.stackpack.Version

class AutoSyncProvision extends ProvisioningScript {
  AutoSyncProvision(ProvisioningContext context) {
    super(context)
  }

  @Override
  ProvisioningIO<scala.Unit> install(Map<String, Object> config) {
    def cleanConfig = cleanupConfig(config)

    def templateArguments = [
      'topicName': topicName(cleanConfig),
      'integrationType': integrationType(cleanConfig),
      'integrationUrl': integrationUrl(cleanConfig),
      'instanceId': context().instance().id()
    ]
    templateArguments.putAll(cleanConfig)

    return context().stackPack().importSnapshot("templates/autosync-template.stj", [:]) >>
           context().instance().importSnapshot("templates/autosync-instance-template.stj", templateArguments)
  }

  @Override
  ProvisioningIO<scala.Unit> upgrade(Map<String, Object> config, Version current) {
    return install(config)
  }

  @Override
  void waitingForData(Map<String, Object> config) {
    context().sts().onDataReceived(topicName(config), {
      context().sts().provisioningComplete()
    })
  }

  private def cleanupConfig(Map<String, Object> config) {
    def cleanConfig = [:]
    cleanConfig.putAll(config)
    cleanConfig.sts_instance_url = config.sts_instance_url.trim()
    cleanConfig.sts_instance_type = config.sts_instance_type.trim()

    return cleanConfig
  }

  private def topicName(Map<String, Object> config) {
    def url = config.sts_instance_url
    def type = config.sts_instance_type
    return context().sts().createTopologyTopicName(type, url)
  }

  private def integrationType(Map<String, Object> config) {
    return config.sts_instance_type
  }

  private def integrationUrl(Map<String, Object> config) {
    return config.sts_instance_url
  }
}