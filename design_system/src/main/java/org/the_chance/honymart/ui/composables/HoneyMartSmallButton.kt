package org.the_chance.honymart.ui.composables

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.the_chance.design_system.R
import org.the_chance.honymart.ui.theme.HoneyMartTheme
import org.the_chance.honymart.ui.theme.Typography
import org.the_chance.honymart.ui.theme.dimens

@Composable
fun HoneyMartSmallButton(
    onClick: () -> Unit,
    @StringRes labelIdStringRes: Int,
    modifier: Modifier = Modifier,
    isEnable: Boolean = true,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    background: Color = MaterialTheme.colorScheme.primary,
) {
    Button(
        onClick = onClick,
        enabled = isEnable,
        modifier = modifier
            .wrapContentWidth()
            .height(MaterialTheme.dimens.heightPrimaryButton),
        colors = ButtonDefaults.buttonColors(
            containerColor = background,
            contentColor = contentColor,
            disabledContentColor = contentColor,
            disabledContainerColor = background.copy(.5F),
        )
    ) {
        Text(
            text = stringResource(id = labelIdStringRes),
            style = Typography.bodyMedium,
            color = contentColor
        )
    }

}

@Preview
@Composable
private fun CustomButtonSmallPreview() {
    HoneyMartTheme {
        HoneyMartSmallButton(labelIdStringRes = R.string.Sign_up, onClick = {})
    }
}

