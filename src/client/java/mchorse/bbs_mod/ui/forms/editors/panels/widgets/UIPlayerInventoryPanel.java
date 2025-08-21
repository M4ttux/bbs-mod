package mchorse.bbs_mod.ui.forms.editors.panels.widgets;

import java.util.function.Consumer;

import mchorse.bbs_mod.l10n.keys.IKey;
import mchorse.bbs_mod.ui.framework.UIContext;
import mchorse.bbs_mod.ui.framework.elements.UIElement;
import mchorse.bbs_mod.ui.framework.elements.overlay.UIOverlayPanel;
import mchorse.bbs_mod.ui.framework.elements.utils.UILabel;
import mchorse.bbs_mod.ui.utils.UI;
import mchorse.bbs_mod.ui.utils.UIUtils;
import mchorse.bbs_mod.utils.colors.Colors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;

public class UIPlayerInventoryPanel extends UIOverlayPanel
{
    /* ---------- Layout & Style ---------- */
    private static final int PANEL_WIDTH      = 240;
    private static final int PADDING_X        = 16;
    private static final int PADDING_Y        = 16;
    private static final int SECTION_GAP_Y    = 16;
    private static final int TITLE_HEIGHT     = 14;

    private static final int SLOT_SIZE        = 18;
    private static final int SLOT_SPACING     = 20;    // separación “visual” entre slots
    private static final int GRID_COLS        = 9;

    // Colores “discretos” estilo vanilla UI
    private static final int SLOT_BG_EMPTY    = 0x66000000;
    private static final int SLOT_BG_FILLED   = 0x66333333;
    private static final int SLOT_BG_HOVER    = 0x88555555;
    private static final int SLOT_BORDER      = 0xFF666666;
    private static final int SLOT_HOVER_TINT  = 0x33FFFFFF;

    private static final int EMPTY_BG         = 0x55333333;
    private static final int EMPTY_BG_HOVER   = 0x77555555;

    /* ---------- Data ---------- */
    private final Consumer<ItemStack> callback;
    private final PlayerInventory playerInventory;

    public UIPlayerInventoryPanel(Consumer<ItemStack> callback)
    {
        super(IKey.constant("Select Item from your inventory"));

        this.callback = callback;

        MinecraftClient mc = MinecraftClient.getInstance();
        this.playerInventory = (mc.player != null) ? mc.player.getInventory() : null;

        this.setupUI();
    }

    /* ---------- UI ---------- */
    private void setupUI()
    {
        // Ancho fijo, el alto se calcula al final según contenido
        this.content.w(PANEL_WIDTH);

        int cursorY = PADDING_Y;

        /* --- Sección: Slot “Empty” --- */
        // Título
        UILabel emptyTitle = sectionTitle("Selection");
        centerHorizontally(emptyTitle, TITLE_HEIGHT, cursorY);
        this.content.add(emptyTitle);
        cursorY += TITLE_HEIGHT + 8;

        // Slot vacío
        UIEmptySlot emptySlot = new UIEmptySlot();
        centerSlot(emptySlot, cursorY);
        this.content.add(emptySlot);
        cursorY += SLOT_SIZE + 6;

        UILabel emptyLabel = UI.label(IKey.constant("Empty"));
        centerLabel(emptyLabel, 12, cursorY);
        this.content.add(emptyLabel);
        cursorY += 12 + SECTION_GAP_Y;

        if (this.playerInventory == null)
        {
            // Si no hay inventario, solo definimos el alto y salimos
            this.content.h(cursorY + PADDING_Y);
            return;
        }

        /* --- Sección: Hotbar --- */
        UILabel hotbarTitle = sectionTitle("Hotbar");
        centerHorizontally(hotbarTitle, TITLE_HEIGHT, cursorY);
        this.content.add(hotbarTitle);
        cursorY += TITLE_HEIGHT + 8;

        int gridStartX = gridStartX(PANEL_WIDTH, GRID_COLS, SLOT_SPACING, SLOT_SIZE);
        // 1 fila (slots 0..8)
        cursorY = placeGridRow(cursorY, gridStartX, 0, 9);

        cursorY += SECTION_GAP_Y;

        /* --- Sección: Main Inventory --- */
        UILabel mainTitle = sectionTitle("Main Inventory");
        centerHorizontally(mainTitle, TITLE_HEIGHT, cursorY);
        this.content.add(mainTitle);
        cursorY += TITLE_HEIGHT + 8;

        // 3 filas (9..35)
        int startIndex = 9;
        for (int row = 0; row < 3; row++)
        {
            cursorY = placeGridRow(cursorY, gridStartX, startIndex + row * 9, 9);
        }

        // Alto final del panel (contenido + padding inferior)
        this.content.h(cursorY + PADDING_Y);
    }

    /* ---------- Helpers de layout ---------- */

    private UILabel sectionTitle(String text)
    {
        UILabel label = UI.label(IKey.constant(text));
        label.w(PANEL_WIDTH).h(TITLE_HEIGHT);
        return label;
    }

    private void centerHorizontally(UIElement element, int h, int y)
    {
        element.relative(this.content).x(0.5F, -PANEL_WIDTH / 2 + PADDING_X).y(y).w(PANEL_WIDTH - PADDING_X * 2).h(h);
    }

    private void centerLabel(UILabel label, int h, int y)
    {
        label.relative(this.content).x(0.5F, -40).y(y).w(80).h(h);
    }

    private void centerSlot(UIElement slot, int y)
    {
        slot.relative(this.content).x(0.5F, -SLOT_SIZE / 2).y(y).w(SLOT_SIZE).h(SLOT_SIZE);
    }

    private int gridStartX(int panelW, int cols, int spacing, int slotSize)
    {
        int gridWidth = cols * spacing;
        // centramos la “rejilla visual” y luego centramos el slot dentro de cada celda
        return (panelW - gridWidth) / 2 + (spacing - slotSize) / 2;
    }

    /** Pone una fila de N slots empezando en indexStart. Devuelve el nuevo cursorY (debajo de la fila). */
    private int placeGridRow(int cursorY, int startX, int indexStart, int count)
    {
        for (int i = 0; i < count; i++)
        {
            UIInventorySlot slot = new UIInventorySlot(indexStart + i);
            slot.relative(this.content).x(startX + i * SLOT_SPACING).y(cursorY).w(SLOT_SIZE).h(SLOT_SIZE);
            this.content.add(slot);
        }
        return cursorY + SLOT_SPACING; // avanzamos una “altura de celda”
    }

    /* ---------- Slots ---------- */

    private class UIInventorySlot extends UIElement
    {
        private final int slotIndex;

        public UIInventorySlot(int slotIndex)
        {
            this.slotIndex = slotIndex;
        }

        private ItemStack getStack()
        {
            if (playerInventory == null || slotIndex < 0 || slotIndex >= playerInventory.size())
            {
                return ItemStack.EMPTY;
            }
            return playerInventory.getStack(slotIndex);
        }

        @Override
        protected boolean subMouseClicked(UIContext context)
        {
            if (this.area.isInside(context) && context.mouseButton == 0)
            {
                ItemStack stack = this.getStack();
                if (!stack.isEmpty() && callback != null)
                {
                    callback.accept(stack.copy());
                    UIPlayerInventoryPanel.this.close();
                    UIUtils.playClick();
                    return true;
                }
            }
            return super.subMouseClicked(context);
        }

        @Override
        public void render(UIContext context)
        {
            ItemStack stack = this.getStack();
            boolean isEmpty  = stack.isEmpty();
            boolean hovered  = this.area.isInside(context);

            int bg = isEmpty ? SLOT_BG_EMPTY : SLOT_BG_FILLED;
            if (hovered && !isEmpty) bg = SLOT_BG_HOVER;

            // fondo y borde
            context.batcher.box(this.area.x, this.area.y, this.area.ex(), this.area.ey(), bg);
            context.batcher.outline(this.area.x, this.area.y, this.area.ex(), this.area.ey(), SLOT_BORDER);

            if (!isEmpty)
            {
                int itemX = this.area.x + 1;
                int itemY = this.area.y + 1;
                context.batcher.getContext().drawItem(stack, itemX, itemY);
                context.batcher.getContext().drawItemInSlot(context.batcher.getFont().getRenderer(), stack, itemX, itemY);

                if (hovered)
                {
                    context.batcher.box(this.area.x, this.area.y, this.area.ex(), this.area.ey(), SLOT_HOVER_TINT);
                }
            }

            super.render(context);
        }
    }

    private class UIEmptySlot extends UIElement
    {
        @Override
        protected boolean subMouseClicked(UIContext context)
        {
            if (this.area.isInside(context) && context.mouseButton == 0)
            {
                if (callback != null)
                {
                    callback.accept(ItemStack.EMPTY);
                    UIPlayerInventoryPanel.this.close();
                    UIUtils.playClick();
                    return true;
                }
            }
            return super.subMouseClicked(context);
        }

        @Override
        public void render(UIContext context)
        {
            boolean hovered = this.area.isInside(context);

            int bg = hovered ? EMPTY_BG_HOVER : EMPTY_BG;
            context.batcher.box(this.area.x, this.area.y, this.area.ex(), this.area.ey(), bg);
            context.batcher.outline(this.area.x, this.area.y, this.area.ex(), this.area.ey(), SLOT_BORDER);

            // “X” muy sutil, centrada
            int cx = this.area.mx();
            int cy = this.area.my();
            context.batcher.box(cx - 6, cy - 1, cx + 6, cy + 1, Colors.WHITE);
            context.batcher.box(cx - 1, cy - 6, cx + 1, cy + 6, Colors.WHITE);

            super.render(context);
        }
    }
}
