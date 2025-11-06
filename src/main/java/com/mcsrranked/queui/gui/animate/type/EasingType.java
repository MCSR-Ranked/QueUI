package com.mcsrranked.queui.gui.animate.type;

public enum EasingType {
    LINEAR {
        @Override
        protected float apply(float t, float strength) {
            return t;
        }
    },

    EASE_IN {
        @Override
        protected float apply(float t, float strength) {
            return (float) Math.pow(t, 2 + strength);
        }
    },

    EASE_OUT {
        @Override
        protected float apply(float t, float strength) {
            return 1 - (float) Math.pow(1 - t, 2 + strength);
        }
    },

    EASE_IN_OUT {
        @Override
        protected float apply(float t, float strength) {
            float p = 2 + strength;
            if (t < 0.5f)
                return (float) Math.pow(2 * t, p) / 2f;
            return 1 - (float) Math.pow(-2 * t + 2, p) / 2f;
        }
    },

    BACK_IN {
        @Override
        protected float apply(float t, float strength) {
            float s = 1.70158f * strength;
            return t * t * ((s + 1) * t - s);
        }
    },

    BACK_OUT {
        @Override
        protected float apply(float t, float strength) {
            float s = 1.70158f * strength;
            return 1 + (float) Math.pow(t - 1, 2) * ((s + 1) * (t - 1) + s);
        }
    },

    BACK_IN_OUT {
        @Override
        protected float apply(float t, float strength) {
            float s = 1.70158f * 1.525f * strength;
            t *= 2;
            if (t < 1)
                return 0.5f * (t * t * ((s + 1) * t - s));
            t -= 2;
            return 0.5f * (t * t * ((s + 1) * t + s) + 2);
        }
    },

    ELASTIC_IN {
        @Override
        protected float apply(float t, float strength) {
            if (t == 0 || t == 1) return t;
            float c4 = (float) ((2 * Math.PI) / (0.3f / strength));
            return (float) (-Math.pow(2, 10 * (t - 1)) * Math.sin((t - 1.075f) * c4));
        }
    },

    ELASTIC_OUT {
        @Override
        protected float apply(float t, float strength) {
            if (t == 0 || t == 1) return t;
            float c4 = (float) ((2 * Math.PI) / (0.3f / strength));
            return (float) (Math.pow(2, -10 * t) * Math.sin((t - 0.075f) * c4) + 1);
        }
    },

    ELASTIC_IN_OUT {
        @Override
        protected float apply(float t, float strength) {
            if (t == 0 || t == 1) return t;
            float c5 = (float) ((2 * Math.PI) / (0.45f / strength));
            t *= 2;
            if (t < 1)
                return (float) (-0.5f * Math.pow(2, 10 * (t - 1)) * Math.sin((t - 1.1125f) * c5));
            return (float) (Math.pow(2, -10 * (t - 1)) * Math.sin((t - 1.1125f) * c5) * 0.5f + 1);
        }
    },

    BOUNCE_OUT {
        @Override
        protected float apply(float t, float strength) {
            float n1 = 7.5625f * strength;
            float d1 = 2.75f;
            if (t < 1 / d1) {
                return n1 * t * t;
            } else if (t < 2 / d1) {
                t -= 1.5f / d1;
                return n1 * t * t + 0.75f;
            } else if (t < 2.5 / d1) {
                t -= 2.25f / d1;
                return n1 * t * t + 0.9375f;
            } else {
                t -= 2.625f / d1;
                return n1 * t * t + 0.984375f;
            }
        }
    },

    BOUNCE_IN {
        @Override
        protected float apply(float t, float strength) {
            return 1 - BOUNCE_OUT.apply(1 - t, strength);
        }
    },

    BOUNCE_IN_OUT {
        @Override
        protected float apply(float t, float strength) {
            if (t < 0.5f)
                return (1 - BOUNCE_OUT.apply(1 - 2 * t, strength)) * 0.5f;
            return BOUNCE_OUT.apply(2 * t - 1, strength) * 0.5f + 0.5f;
        }
    };

    protected abstract float apply(float t, float strength);

    public float ease(float t) {
        return ease(t, 1f);
    }

    public float ease(float t, float strength) {
        t = Math.max(0f, Math.min(1f, t));
        return apply(t, Math.max(0.01f, strength));
    }
}
