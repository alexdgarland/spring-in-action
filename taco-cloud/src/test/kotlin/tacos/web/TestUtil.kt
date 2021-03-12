package tacos.web

import org.hamcrest.CoreMatchers
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

fun ResultActions.andExpectHasString(s: String) = andExpect (
    MockMvcResultMatchers
        .content()
        .string(CoreMatchers.containsString(s))
)
