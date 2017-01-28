package com.fly.game;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.fly.game.screen.object.Coin;
import com.fly.game.screen.object.Hero;

public class WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
    }

    @Override
    public void endContact(Contact contact) {

        Fixture fiA = contact.getFixtureA();
        Fixture fiB = contact.getFixtureB();

        if (fiA.getUserData() == "dodj" && fiB.getUserData() instanceof Hero) {
            ((Hero) fiB.getUserData()).dodj = false;
            ((Hero) fiB.getUserData()).dodjed = false;
        }

        if (fiB.getUserData() == "dodj" && fiA.getUserData() instanceof Hero) {
            ((Hero) fiA.getUserData()).dodj = false;
            ((Hero) fiA.getUserData()).dodjed = false;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Fixture fiA = contact.getFixtureA();
        Fixture fiB = contact.getFixtureB();

        if (fiA.getUserData() instanceof Coin && fiB.getUserData() instanceof Hero) {
            ((Coin) fiA.getUserData()).collect();
            contact.setEnabled(false);
            return;
        }

        if (fiB.getUserData() instanceof Coin && fiA.getUserData() instanceof Hero) {
            ((Coin) fiB.getUserData()).collect();
            contact.setEnabled(false);
            return;
        }

        if (fiA.getUserData() == "dodj" && fiB.getUserData() instanceof Hero) {
            ((Hero) fiB.getUserData()).dodj = true;
            contact.setEnabled(false);
            return;
        }

        if (fiB.getUserData() == "dodj" && fiA.getUserData() instanceof Hero) {
            ((Hero) fiA.getUserData()).dodj = true;
            contact.setEnabled(false);
            return;
        }

        if (fiA.getUserData() instanceof Hero) {
            if (((Hero) fiA.getUserData()).dodjed) {
                contact.setEnabled(false);
                return;
            }
        }

        if (fiB.getUserData() instanceof Hero) {
            if (((Hero) fiB.getUserData()).dodjed) {
                contact.setEnabled(false);
                return;
            }
        }

        if (fiA.getUserData() instanceof Hero) {
            ((Hero) fiA.getUserData()).kill();
            return;
        }

        if (fiB.getUserData() instanceof Hero) {
            ((Hero) fiB.getUserData()).kill();
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}
