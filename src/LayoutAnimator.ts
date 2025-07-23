// rns-animator\src\LayoutAnimator.ts
import { NativeModules } from 'react-native';

type BaseConfig = {
  duration?: number;
  easing?: EasingType;
};

type SpringConfig = {
  duration?: number;
  damping?: number;
  stiffness?: number;
};

type SharedTransitionConfig = {
  fromViewTag: number;
  toViewTag: number;
  duration?: number;
  easing?: EasingType;
};

type EasingType =
  | 'ease-in'
  | 'ease-out'
  | 'ease-in-out'
  | 'linear'
  | 'bounce'
  | 'anticipate'
  | 'overshoot'
  | 'anticipate-overshoot'
  | 'fast-out-slow-in';

const { LayoutAnimator } = NativeModules;

function callNative(method: keyof typeof LayoutAnimator, args: any[] = []): Promise<boolean> {
  if (!LayoutAnimator || typeof LayoutAnimator[method] !== 'function') {
    return Promise.reject(new Error(`LayoutAnimator method ${method} not available.`));
  }
  return LayoutAnimator[method](...args);
}

export const RNSLayoutAnimator = {
  fadeIn(config: BaseConfig = {}) {
    const { duration = 400, easing = 'ease-in-out' } = config;
    return callNative('fadeIn', [duration, easing]);
  },

  fadeOut(config: BaseConfig = {}) {
    const { duration = 400, easing = 'ease-in-out' } = config;
    return callNative('fadeOut', [duration, easing]);
  },

  slideOnReorder(config: BaseConfig = {}) {
    const { duration = 400, easing = 'ease-in-out' } = config;
    return callNative('slideOnReorder', [duration, easing]);
  },

  slideFromLeft(config: BaseConfig = {}) {
    const { duration = 400, easing = 'ease-out' } = config;
    return callNative('slideFromLeft', [duration, easing]);
  },

  slideFromRight(config: BaseConfig = {}) {
    const { duration = 400, easing = 'ease-in' } = config;
    return callNative('slideFromRight', [duration, easing]);
  },

  explode(config: BaseConfig = {}) {
    const { duration = 400, easing = 'linear' } = config;
    return callNative('explode', [duration, easing]);
  },

  scaleUp(config: BaseConfig = {}) {
    const { duration = 500, easing = 'ease-out' } = config;
    return callNative('scaleUp', [duration, easing]);
  },

  magicMove(config: BaseConfig = {}) {
    const { duration = 600, easing = 'fast-out-slow-in' } = config;
    return callNative('magicMove', [duration, easing]);
  },

  fanOut(config: BaseConfig = {}) {
    const { duration = 500, easing = 'ease-in-out' } = config;
    return callNative('fanOut', [duration, easing]);
  },

  spring(config: SpringConfig = {}) {
    const { duration = 500, damping = 1.2, stiffness = 7.0 } = config;
    return callNative('spring', [duration, damping, stiffness]);
  },

  sharedTransition(config: SharedTransitionConfig) {
    const { fromViewTag, toViewTag, duration = 500, easing = 'ease-in-out' } = config;
    return callNative('sharedTransition', [fromViewTag, toViewTag, duration, easing]);
  },
};
