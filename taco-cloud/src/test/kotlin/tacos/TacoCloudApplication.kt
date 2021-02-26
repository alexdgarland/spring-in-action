package tacos

import org.springframework.boot.autoconfigure.SpringBootApplication

// Workaround to avoid having WebMvcTest classes (etc) use the main TacoCloudApplication class and fall over on empty JPA metamodel
// TODO - try and get working with Configuration classes for slices instead (this hasn't worked so far...)

@SpringBootApplication
class TacoCloudApplication
