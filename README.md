### 📚 `README.md` Starter

````md
# rns-animator 🚀

Native layout animations for React Native using Android TransitionManager — fade, slide, explode, spring, and more!

## Installation

```bash
npm install rns-animator
npx expo prebuild
```
````

## Usage

```tsx
import RNSLayoutAnimator from "rns-animator";

RNSLayoutAnimator.fadeIn({ duration: 300 });
RNSLayoutAnimator.spring({ damping: 1.2, stiffness: 7 });
```

> Works with custom dev clients, not Expo Go

## Animations Available

- `fadeIn()`
- `fadeOut()`
- `slideOnReorder()`
- `explode()`
- `magicMove()`
- `spring()`
- `sharedTransition()`

````

---

When you’re ready, hit:

```bash
npm publish --access public
````

Let me know if you want to do the whole release flow together — versioning, changelog, GitHub tagging, and all the sweet polish that makes packages sparkle ✨
