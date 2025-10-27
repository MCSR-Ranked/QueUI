package com.mcsrranked.queui.mixin.test;

import com.mcsrranked.queui.test.TestScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(TitleScreen.class)
public class MixinTitleScreen extends Screen {

    protected MixinTitleScreen(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    public void onInit(CallbackInfo ci) {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            this.addButton(new ButtonWidget(10, 10, 80, 20, new LiteralText("QueUI Test"),
                    button -> Objects.requireNonNull(this.client).openScreen(new TestScreen())));
        }
    }
}
