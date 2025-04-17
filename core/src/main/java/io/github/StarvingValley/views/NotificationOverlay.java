package io.github.StarvingValley.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;

import io.github.StarvingValley.models.events.EventBus;
import io.github.StarvingValley.models.events.NotificationEvent;
import io.github.StarvingValley.utils.TileUtils;

public class NotificationOverlay {
    List<NotificationEvent> notifications;

    private final EventBus eventBus;
    private final BitmapFont font = new BitmapFont();
    private final SpriteBatch batch = new SpriteBatch();

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();
    private final GlyphLayout layout = new GlyphLayout();

    private static final int MAX_TEXT_WIDTH = 8 * TileUtils.getTileWidth();
    private static final int PADDING = 20;
    private static final int Y_OFFSET = 50;
    private static final int CORNER_RADIUS = 20;
    private static final Color BG_COLOR = new Color(0.2f, 0.2f, 0.2f, 1);

    public NotificationOverlay(EventBus eventBus) {
        this.eventBus = eventBus;
        this.notifications = new LinkedList<>();

        font.getData().setScale(5f);
    }

    public void render() {
        List<NotificationEvent> notificationEvents = eventBus.getEvents(NotificationEvent.class);
        notifications.addAll(notificationEvents);
        notifications.removeIf(n -> Instant.now().isAfter(n.expiryTime));
        if (notifications.isEmpty()) {
            return;
        }

        StringBuilder notificationMessage = new StringBuilder();
        for (int i = 0; i < notifications.size(); i++) {
            notificationMessage.append("> ").append(notifications.get(i).message);
            if (i < notifications.size() - 1) {
                notificationMessage.append('\n');
            }
        }

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        layout.setText(font, notificationMessage.toString(), Color.WHITE, MAX_TEXT_WIDTH, Align.left, true);
        float textX = (screenWidth - layout.width) / 2;
        float textY = screenHeight - Y_OFFSET;

        // Draw background box
        float boxX = textX - PADDING;
        float boxY = textY - layout.height - PADDING;
        float boxWidth = layout.width + (PADDING * 2);
        float boxHeight = layout.height + (PADDING * 2);

        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(BG_COLOR);
        shapeRenderer.rect(boxX + CORNER_RADIUS, boxY, boxWidth - 2*CORNER_RADIUS, boxHeight);
        shapeRenderer.rect(boxX, boxY + CORNER_RADIUS, boxWidth, boxHeight - 2*CORNER_RADIUS);
        shapeRenderer.circle(boxX + CORNER_RADIUS, boxY + CORNER_RADIUS, CORNER_RADIUS);
        shapeRenderer.circle(boxX + boxWidth - CORNER_RADIUS, boxY + CORNER_RADIUS, CORNER_RADIUS);
        shapeRenderer.circle(boxX + CORNER_RADIUS, boxY + boxHeight - CORNER_RADIUS, CORNER_RADIUS);
        shapeRenderer.circle(boxX + boxWidth - CORNER_RADIUS, boxY + boxHeight - CORNER_RADIUS, CORNER_RADIUS);
        shapeRenderer.end();

        // Draw text
        batch.begin();
        font.draw(batch, layout, textX, textY);
        batch.end();
    }
}

