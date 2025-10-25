package com.mcsrranked.queui.gui.widget;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.AbstractParentElement;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.TickableElement;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;

import java.util.*;

public class PaginationWidget extends AbstractParentElement implements Drawable, TickableElement {

    private boolean alwaysVisibleSelects = true;
    private int currentPage = 0;
    private final Map<Integer, AbstractButtonWidget> pageSelectMap = new HashMap<>();
    private final Map<Integer, Set<Element>> pageElementMap = new HashMap<>();
    private final Map<Integer, Drawable> pagePreRenderMap = new HashMap<>();
    private final Map<Integer, Drawable> pageRenderMap = new HashMap<>();
    private final Map<Integer, Runnable> pageChangeListenerMap = new HashMap<>();

    public void clear() {
        this.pagePreRenderMap.clear();
        this.pageRenderMap.clear();
        this.pageElementMap.clear();
        this.pageSelectMap.clear();
        this.pageChangeListenerMap.clear();
    }

    public <T extends AbstractButtonWidget> void setPageSelectButton(int page, T button) {
        pageSelectMap.put(page, button);
        button.active = this.getCurrentPage() != page;
    }

    public <T extends Element> T addElement(int page, T element) {
        if (!pageElementMap.containsKey(page)) pageElementMap.put(page, new LinkedHashSet<>());

        pageElementMap.get(page).add(element);
        return element;
    }

    public void setRenderPage(int page, Drawable drawable) {
        pageRenderMap.put(page, drawable);
    }

    public void setPreRenderPage(int page, Drawable drawable) {
        pagePreRenderMap.put(page, drawable);
    }

    public void setPageChangeListener(int page, Runnable runnable) {
        pageChangeListenerMap.put(page, runnable);
    }

    public AbstractButtonWidget getPageSelectButton(int page) {
        return this.pageSelectMap.get(page);
    }

    public boolean isAlwaysVisibleSelects() {
        return alwaysVisibleSelects;
    }

    public void setAlwaysVisibleSelects(boolean alwaysVisibleSelects) {
        this.alwaysVisibleSelects = alwaysVisibleSelects;
    }

    public List<Element> getElementsInCurrentPage() {
        List<Element> elements = Lists.newArrayList();
        if (this.isAlwaysVisibleSelects()) {
            for (AbstractButtonWidget value : this.pageSelectMap.values()) {
                if (value.visible) elements.add(value);
            }
        }
        if (this.pageElementMap.containsKey(this.getCurrentPage()))
            elements.addAll(this.pageElementMap.get(this.getCurrentPage()));
        return elements;
    }

    public void setPage(int page) {
        if (pageSelectMap.containsKey(this.getCurrentPage())) pageSelectMap.get(this.getCurrentPage()).active = true;
        for (Element element : this.getElementsInCurrentPage()) {
            if (element instanceof AbstractButtonWidget) {
                ((AbstractButtonWidget) element).setFocused(false);
            }
        }
        this.currentPage = page;
        if (pageSelectMap.containsKey(this.getCurrentPage())) pageSelectMap.get(this.getCurrentPage()).active = false;
        if (pageChangeListenerMap.containsKey(this.getCurrentPage())) pageChangeListenerMap.get(this.getCurrentPage()).run();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (pagePreRenderMap.containsKey(this.getCurrentPage())) pagePreRenderMap.get(this.getCurrentPage()).render(matrices, mouseX, mouseY, delta);

        for (Element element : this.getElementsInCurrentPage()) {
            if (element instanceof Drawable) {
                ((Drawable) element).render(matrices, mouseX, mouseY, delta);
            }
        }

        if (pageRenderMap.containsKey(this.getCurrentPage())) pageRenderMap.get(this.getCurrentPage()).render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public List<? extends Element> children() {
        return this.getElementsInCurrentPage();
    }

    @Override
    public void tick() {
        for (Element element : this.getElementsInCurrentPage()) {
            if (element instanceof TickableElement) {
                ((TickableElement) element).tick();
            }
            if (element instanceof TextFieldWidget) {
                ((TextFieldWidget) element).tick();
            }
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        for (Element element : this.getElementsInCurrentPage()) {
            if (element instanceof EntryListWidget) {
                element.mouseScrolled(mouseX, mouseY, amount);
            }
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }
}
