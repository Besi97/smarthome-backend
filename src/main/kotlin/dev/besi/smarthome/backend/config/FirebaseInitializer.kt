package dev.besi.smarthome.backend.config

import com.google.api.gax.core.CredentialsProvider
import com.google.api.gax.core.GoogleCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.spring.core.GcpProjectIdProvider
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.ContextStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
class FirebaseInitializer(
        @Autowired val projectIdProvider: GcpProjectIdProvider
) {

    @PostConstruct
    fun onApplicationStart() {
        FirebaseApp.initializeApp(
                FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.getApplicationDefault())
                        .setProjectId(projectIdProvider.projectId)
                        .build()
        )
    }

}
