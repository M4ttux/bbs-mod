package mchorse.bbs_mod.ui.framework.elements.input.keyframes.factories;

import mchorse.bbs_mod.settings.values.base.BaseValue;
import mchorse.bbs_mod.ui.UIKeys;
import mchorse.bbs_mod.ui.framework.elements.buttons.UIIcon;
import mchorse.bbs_mod.ui.framework.elements.buttons.UIButton;
import mchorse.bbs_mod.ui.framework.elements.input.UITrackpad;
import mchorse.bbs_mod.ui.framework.elements.input.keyframes.UIKeyframes;
import mchorse.bbs_mod.ui.utils.UI;
import mchorse.bbs_mod.ui.utils.icons.Icons;
import mchorse.bbs_mod.utils.keyframes.Keyframe;
import mchorse.bbs_mod.utils.keyframes.KeyframeChannel;

public class UIDoubleKeyframeFactory extends UIKeyframeFactory<Double>
{
    private UITrackpad value;
    private UITrackpad lx;
    private UITrackpad ly;
    private UITrackpad rx;
    private UITrackpad ry;
    private UIButton killButton;

    public UIDoubleKeyframeFactory(Keyframe<Double> keyframe, UIKeyframes editor)
    {
        super(keyframe, editor);

        this.value = new UITrackpad(this::setValue);
        this.lx = new UITrackpad((v) -> BaseValue.edit(this.keyframe, (kf) -> kf.lx = v.floatValue()));
        this.ly = new UITrackpad((v) -> BaseValue.edit(this.keyframe, (kf) -> kf.ly = v.floatValue()));
        this.rx = new UITrackpad((v) -> BaseValue.edit(this.keyframe, (kf) -> kf.rx = v.floatValue()));
        this.ry = new UITrackpad((v) -> BaseValue.edit(this.keyframe, (kf) -> kf.ry = v.floatValue()));
        this.value.setValue(keyframe.getValue());
        this.lx.setValue(keyframe.lx);
        this.ly.setValue(keyframe.ly);
        this.rx.setValue(keyframe.rx);
        this.ry.setValue(keyframe.ry);

        // Create kill button for damage keyframes
        this.killButton = new UIButton(UIKeys.ACTIONS_ATTACK_KILL, (b) -> {
            this.setValue(-1.0);
            this.value.setValue(-1.0);
        });
        this.killButton.tooltip(UIKeys.ACTIONS_ATTACK_KILL_TOOLTIP);

        // Check if this is a damage keyframe by looking at the keyframe's parent channel
        boolean isDamageKeyframe = this.isDamageKeyframe();

        if (isDamageKeyframe) {
            this.scroll.add(this.value, this.killButton,
                UI.row(new UIIcon(Icons.LEFT_HANDLE, null).tooltip(UIKeys.KEYFRAMES_LEFT_HANDLE), this.lx, this.ly),
                UI.row(new UIIcon(Icons.RIGHT_HANDLE, null).tooltip(UIKeys.KEYFRAMES_RIGHT_HANDLE), this.rx, this.ry)
            );
        } else {
            this.scroll.add(this.value,
                UI.row(new UIIcon(Icons.LEFT_HANDLE, null).tooltip(UIKeys.KEYFRAMES_LEFT_HANDLE), this.lx, this.ly),
                UI.row(new UIIcon(Icons.RIGHT_HANDLE, null).tooltip(UIKeys.KEYFRAMES_RIGHT_HANDLE), this.rx, this.ry)
            );
        }
    }

    private boolean isDamageKeyframe() {
        // Try to determine if this is a damage keyframe by examining the parent channel
        try {
            if (this.keyframe.getParent() instanceof KeyframeChannel) {
                KeyframeChannel<?> channel = (KeyframeChannel<?>) this.keyframe.getParent();
                String channelId = channel.getId();
                return channelId != null && channelId.equals("damage");
            }
        } catch (Exception e) {
            // If we can't access the channel, default to false
        }
        return false;
    }

    @Override
    public void update()
    {
        super.update();

        this.value.setValue(keyframe.getValue());
        this.lx.setValue(keyframe.lx);
        this.ly.setValue(keyframe.ly);
        this.rx.setValue(keyframe.rx);
        this.ry.setValue(keyframe.ry);
    }
}